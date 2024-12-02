    package com.example.nhb_doan;

    import androidx.appcompat.app.AppCompatActivity;

    import android.content.Intent;
    import android.os.Bundle;
    import android.view.View;
    import android.widget.Button;
    import android.widget.ListView;
    import android.widget.TextView;

    import com.example.nhb_doan.adapter.DishAdapter;
    import com.example.nhb_doan.model.Dish;

    import java.util.List;

    public class HomeActivity extends AppCompatActivity {
        Button goToDashboardButton, goToLoginButton, goToOrderButton;
        boolean isAdmin;
        private ListView listViewDishes;
        private SQLiteHelper sqLiteHelper;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home);

            goToDashboardButton = findViewById(R.id.goToDashboardButton);
            goToLoginButton = findViewById(R.id.goToLoginButton);
            goToOrderButton = findViewById(R.id.goToOrderButton);

            listViewDishes = findViewById(R.id.listViewDishes);

            sqLiteHelper = new SQLiteHelper(this);
            int userId = getUserId();

            if (userId != -1 && userId == 1) {
                goToDashboardButton.setVisibility(View.VISIBLE);
            } else {
                goToDashboardButton.setVisibility(View.GONE);
            }

            goToDashboardButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this, DashboardActivity.class);
                    startActivity(intent);
                }
            });
            goToLoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            });
            goToOrderButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this, OrderActivity.class);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                }
            });

            List<Dish> dishes = sqLiteHelper.getAllDishes();

            DishAdapter adapter = new DishAdapter(this, dishes);
            listViewDishes.setAdapter(adapter);
        }

        @Override
        protected void onResume() {
            super.onResume();
            List<Dish> dishes = sqLiteHelper.getAllDishes();

            if (dishes.isEmpty()) {
                listViewDishes.setVisibility(View.GONE);
                TextView emptyView = findViewById(R.id.emptyView);
                emptyView.setVisibility(View.VISIBLE);
                emptyView.setText("Hiện tại không có món ăn nào.");
            } else {
                listViewDishes.setVisibility(View.VISIBLE);
                TextView emptyView = findViewById(R.id.emptyView);
                emptyView.setVisibility(View.GONE);

                DishAdapter adapter = new DishAdapter(this, dishes);
                listViewDishes.setAdapter(adapter);
            }
        }

        public int getUserId() {
            return getIntent().getIntExtra("userId", -1);
        }
    }
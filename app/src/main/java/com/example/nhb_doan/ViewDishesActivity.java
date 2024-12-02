package com.example.nhb_doan;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nhb_doan.adapter.DishAdminAdapter;
import com.example.nhb_doan.model.Dish;

import java.util.List;

public class ViewDishesActivity extends AppCompatActivity {
    private SQLiteHelper sqLiteHelper;
    private ListView listViewDishes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_dishes);

        listViewDishes = findViewById(R.id.dish_list_view);
        sqLiteHelper = new SQLiteHelper(this);

        List<Dish> dishes = sqLiteHelper.getAllDishes();

        DishAdminAdapter adapter = new DishAdminAdapter(this, dishes);
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

            DishAdminAdapter adapter = new DishAdminAdapter(this, dishes);
            listViewDishes.setAdapter(adapter);
        }
    }
}

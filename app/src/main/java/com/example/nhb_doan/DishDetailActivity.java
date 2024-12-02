package com.example.nhb_doan;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhb_doan.model.Dish;

public class DishDetailActivity extends AppCompatActivity {
    private SQLiteHelper sqLiteHelper;
    private int dishId, userId;
    private int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_detail);

        sqLiteHelper = new SQLiteHelper(this);
        dishId = getIntent().getIntExtra("dishId", -1);
        userId = getIntent().getIntExtra("userId", -1);

        Dish dish = sqLiteHelper.getDishById(dishId);

        TextView dishName = findViewById(R.id.textViewDishName);
        TextView dishDescription = findViewById(R.id.textViewDescription);
        ImageView imageViewDish = findViewById(R.id.imageViewDish);

        Button increaseButton = findViewById(R.id.increaseQuantity);
        Button decreaseButton = findViewById(R.id.decreaseQuantity);
        TextView quantityView = findViewById(R.id.quantity);
        Button orderButton = findViewById(R.id.orderButton);

        EditText addressEditText = findViewById(R.id.editTextAddress);
        EditText phoneEditText = findViewById(R.id.editTextPhone);

        dishName.setText(dish.getName());
        dishDescription.setText(dish.getDescription());
        imageViewDish.setImageURI(Uri.parse(dish.getImageUri()));
        quantityView.setText(String.valueOf(quantity));

        increaseButton.setOnClickListener(v -> {
            quantity++;
            quantityView.setText(String.valueOf(quantity));
        });

        decreaseButton.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                quantityView.setText(String.valueOf(quantity));
            }
        });

        orderButton.setOnClickListener(v -> {
            if (userId != -1) {
                String address = addressEditText.getText().toString();
                String phone = phoneEditText.getText().toString();

                if (address.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập địa chỉ và số điện thoại", Toast.LENGTH_SHORT).show();
                } else {
                    sqLiteHelper.addOrder(userId, dishId, quantity, "CHUA_GIAO", address, phone);
                    Toast.makeText(this, "Đã đặt món thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else {
                Toast.makeText(this, "Lỗi: Không có ID người dùng", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
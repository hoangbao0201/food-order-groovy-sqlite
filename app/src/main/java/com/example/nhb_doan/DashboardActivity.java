package com.example.nhb_doan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DashboardActivity extends AppCompatActivity {

    Button createDishButton, viewDishesButton, viewOrdersButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        createDishButton = findViewById(R.id.createDishButton);
        viewDishesButton = findViewById(R.id.viewDishesButton);
        viewOrdersButton = findViewById(R.id.viewOrdersButton);

        createDishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, CreateDishActivity.class);
                startActivity(intent);
            }
        });

        viewDishesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, ViewDishesActivity.class);
                startActivity(intent);
            }
        });
        viewOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, ViewOrdersAdminActivity.class);
                startActivity(intent);
            }
        });
    }
}
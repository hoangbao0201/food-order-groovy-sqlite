package com.example.nhb_doan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View;

import com.example.nhb_doan.adapter.OrderAdapter;
import com.example.nhb_doan.model.Order;

import java.util.List;

public class OrderActivity extends AppCompatActivity {
    private ListView listViewOrders;
    private SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        sqLiteHelper = new SQLiteHelper(this);
        int userId = getIntent().getIntExtra("userId", -1);

        listViewOrders = findViewById(R.id.listViewOrders);

        Log.d("OrderActivity", "UserId from intent: " + userId);

        List<Order> orders = sqLiteHelper.getOrdersByUserId(userId);
        if (orders.isEmpty()) {
            Log.d("Orders", "No orders found for user ID: " + userId);
        } else {
            for (Order order : orders) {
                Log.d("Orders", order.toString());
            }
        }

        OrderAdapter adapter = new OrderAdapter(this, orders);
        listViewOrders.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        int userId = getIntent().getIntExtra("userId", -1);

        Log.d("OrderActivity", "UserId from intent (onResume): " + userId);

        List<Order> orders = sqLiteHelper.getOrdersByUserId(userId);

        if (orders.isEmpty()) {
            listViewOrders.setVisibility(View.GONE);
            TextView emptyView = findViewById(R.id.emptyView);
            emptyView.setVisibility(View.VISIBLE);
            emptyView.setText("Chưa có món ăn nào được đặt.");
        } else {
            listViewOrders.setVisibility(View.VISIBLE);
            TextView emptyView = findViewById(R.id.emptyView);
            emptyView.setVisibility(View.GONE);

            OrderAdapter adapter = new OrderAdapter(this, orders);
            listViewOrders.setAdapter(adapter);
        }
    }
}
package com.example.nhb_doan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.nhb_doan.adapter.OrderAdminAdapter;
import com.example.nhb_doan.model.Order;

import java.util.List;

public class ViewOrdersAdminActivity extends AppCompatActivity {
    ListView ordersListView;
    SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders_admin);

        ordersListView = findViewById(R.id.order_list_view);

        sqLiteHelper = new SQLiteHelper(this);
        List<Order> orderList = sqLiteHelper.getAllOrders();

        OrderAdminAdapter orderAdapter = new OrderAdminAdapter(this, orderList);
        ordersListView.setAdapter(orderAdapter);

    }

}
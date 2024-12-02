package com.example.nhb_doan.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nhb_doan.R;
import com.example.nhb_doan.SQLiteHelper;
import com.example.nhb_doan.model.Dish;
import com.example.nhb_doan.model.Order;

import java.util.List;

public class OrderAdapter extends BaseAdapter {
    private Context context;
    Button deleteButton;
    private List<Order> orders;
    private LayoutInflater inflater;
    private SQLiteHelper sqLiteHelper;

    public OrderAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
        this.inflater = LayoutInflater.from(context);
        this.sqLiteHelper = new SQLiteHelper(context);
    }

    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public Object getItem(int position) {
        return orders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.order_list_item, parent, false);
        }

        Order order = orders.get(position);

        ImageView imageViewDishOrder = convertView.findViewById((R.id.imageViewDishOrder));
        TextView textViewDishName = convertView.findViewById(R.id.textViewDishNameOrder);
        TextView textViewQuantity = convertView.findViewById(R.id.textViewQuantityOrder);
        TextView textViewStatus = convertView.findViewById(R.id.textViewStatusOrder);
        TextView textViewAddress = convertView.findViewById(R.id.textViewAddressOrder);
        TextView textViewPhoneNumber = convertView.findViewById(R.id.textViewPhoneNumberOrder);

        Button deleteButton = convertView.findViewById(R.id.deleteButtonOrder);

        Dish dish = sqLiteHelper.getDishById(order.getDishId());

        imageViewDishOrder.setImageURI(Uri.parse(dish.getImageUri()));
        textViewDishName.setText(dish.getName());
        textViewQuantity.setText("Số lượng: " + order.getQuantity());
        textViewAddress.setText("Địa chỉ: " + order.getShippingAddress());
        textViewPhoneNumber.setText("Số điện thoại: " + order.getPhoneNumber());

        String[] statuses = {"CHUA_GIAO", "DANG_LAM", "DANG_GIAO", "DA_GIAO"};
        int[] colors = {
                context.getResources().getColor(android.R.color.darker_gray),    // CHUA_GIAO: Xám
                context.getResources().getColor(android.R.color.holo_orange_light), // DANG_LAM: Cam
                context.getResources().getColor(android.R.color.holo_blue_light),   // DANG_GIAO: Xanh dương
                context.getResources().getColor(android.R.color.holo_green_light)  // DA_GIAO: Xanh lá
        };
        String currentStatus = order.getStatus();

        int currentIndex = java.util.Arrays.asList(statuses).indexOf(currentStatus);
        textViewStatus.setText(getStatusText(currentStatus));
        textViewStatus.setBackgroundColor(colors[currentIndex]);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteOrder(order.getId());
            }
        });

        return convertView;
    }
    private String getStatusText(String status) {
        switch (status) {
            case "CHUA_GIAO":
                return "Chưa giao";
            case "DANG_LAM":
                return "Đang làm";
            case "DANG_GIAO":
                return "Đang giao";
            case "DA_GIAO":
                return "Đã giao";
            default:
                return status;
        }
    }

    private void deleteOrder(final int oderId) {
        new AlertDialog.Builder(context)
                .setTitle("Xóa đặt món")
                .setMessage("Bạn có chắc chắn muốn xóa đặt món này không?")
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        sqLiteHelper.deleteOrder(oderId);

                        for (int i = 0; i < orders.size(); i++) {
                            if (orders.get(i).getId() == oderId) {
                                orders.remove(i);
                                break;
                            }
                        }

                        notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}

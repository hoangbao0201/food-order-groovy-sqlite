package com.example.nhb_doan.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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

public class OrderAdminAdapter extends BaseAdapter {
    private Context context;
    Button deleteButton;
    private List<Order> orders;
    private LayoutInflater inflater;
    private SQLiteHelper sqLiteHelper;

    public OrderAdminAdapter(Context context, List<Order> orders) {
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
            convertView = inflater.inflate(R.layout.order_admin_list_item, parent, false);
        }

        Order order = orders.get(position);

        // Liên kết các thành phần giao diện
        ImageView imageViewDishOrder = convertView.findViewById(R.id.imageViewDishAdminOrder);
        TextView textViewDishName = convertView.findViewById(R.id.textViewDishNameAdminOrder);
        TextView textViewQuantity = convertView.findViewById(R.id.textViewQuantityAdminOrder);

        TextView textViewAddress = convertView.findViewById(R.id.textViewAddressAdminOrder);
        TextView textViewPhoneNumber = convertView.findViewById(R.id.textViewPhoneNumberAdminOrder);

        Button deleteButton = convertView.findViewById(R.id.deleteButtonAdminOrder);
        Button changeStatusButton = convertView.findViewById(R.id.changeStatusButtonAdminOrder);

        // Lấy thông tin món ăn từ cơ sở dữ liệu
        Dish dish = sqLiteHelper.getDishById(order.getDishId());
        if (dish != null) {
            imageViewDishOrder.setImageURI(Uri.parse(dish.getImageUri()));
            textViewDishName.setText(dish.getName());
        }

        textViewDishName.setText(dish.getName());
        imageViewDishOrder.setImageURI(Uri.parse(dish.getImageUri()));
        textViewQuantity.setText("Số lượng: " + order.getQuantity());
        textViewAddress.setText("Địa chỉ: " + order.getShippingAddress());
        textViewPhoneNumber.setText("Số điện thoại: " + order.getPhoneNumber());

        // Cấu hình trạng thái và màu sắc
        String[] statuses = {"CHUA_GIAO", "DANG_LAM", "DANG_GIAO", "DA_GIAO"};
        int[] colors = {
                context.getResources().getColor(android.R.color.darker_gray),    // CHUA_GIAO: Xám
                context.getResources().getColor(android.R.color.holo_orange_light), // DANG_LAM: Cam
                context.getResources().getColor(android.R.color.holo_blue_light),   // DANG_GIAO: Xanh dương
                context.getResources().getColor(android.R.color.holo_green_light)  // DA_GIAO: Xanh lá
        };

        String currentStatus = order.getStatus();
        int currentIndex = java.util.Arrays.asList(statuses).indexOf(currentStatus);
        changeStatusButton.setText(getStatusText(currentStatus));
        changeStatusButton.setBackgroundColor(colors[currentIndex]);

        // Xử lý nút xóa đơn hàng
        deleteButton.setOnClickListener(v -> deleteOrder(order.getId()));

        // Xử lý nút thay đổi trạng thái
        changeStatusButton.setOnClickListener(v -> {
            if (currentIndex < statuses.length - 1) {
                String newStatus = statuses[currentIndex + 1];
                int newColor = colors[currentIndex + 1];

                new AlertDialog.Builder(context)
                        .setTitle("Thay đổi trạng thái")
                        .setMessage("Bạn có muốn đổi trạng thái sang " + newStatus + " không?")
                        .setPositiveButton("Có", (dialog, which) -> {
                            order.setStatus(newStatus);
                            sqLiteHelper.updateOrderStatus(order.getId(), newStatus);

                            // Cập nhật giao diện
                            changeStatusButton.setText(newStatus);
                            changeStatusButton.setBackgroundColor(newColor);

                            orders.set(position, order);

                            if ("DA_GIAO".equals(newStatus)) {
                                changeStatusButton.setEnabled(false);
                            }
                            notifyDataSetChanged();
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            } else {
                changeStatusButton.setEnabled(false); // Vô hiệu hóa khi đã hoàn tất
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
                .setMessage("Bạn có chắc chắn muốn xóa đặt món này?")
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

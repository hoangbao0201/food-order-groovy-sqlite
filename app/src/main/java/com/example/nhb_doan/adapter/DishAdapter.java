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

import com.example.nhb_doan.DishDetailActivity;
import com.example.nhb_doan.HomeActivity;
import com.example.nhb_doan.R;
import com.example.nhb_doan.SQLiteHelper;
import com.example.nhb_doan.model.Dish;

import java.util.List;

public class DishAdapter extends BaseAdapter {
    private Context context;

    Button editButton, deleteButton, viewDetailButton;
    private List<Dish> dishes;
    private LayoutInflater inflater;
    private SQLiteHelper sqLiteHelper;

    public DishAdapter(Context context, List<Dish> dishes) {
        this.context = context;
        this.dishes = dishes;
        this.inflater = LayoutInflater.from(context);
        this.sqLiteHelper = new SQLiteHelper(context);
    }

    @Override
    public int getCount() {
        return dishes.size();
    }

    @Override
    public Object getItem(int position) {
        return dishes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.dish_list_item, parent, false);
        }

        Dish dish = dishes.get(position);

        ImageView imageView = convertView.findViewById(R.id.imageViewDish);
        TextView textViewName = convertView.findViewById(R.id.textViewDishName);
        TextView textViewDescription = convertView.findViewById(R.id.textViewDescription);

        viewDetailButton = convertView.findViewById(R.id.viewDetailButton);

        textViewName.setText(dish.getName());
        textViewDescription.setText(dish.getDescription());
        imageView.setImageURI(Uri.parse(dish.getImageUri()));

        viewDetailButton.setOnClickListener(v -> viewDishDetails(dish));

        return convertView;
    }

    private void viewDishDetails(Dish dish) {
        Intent intent = new Intent(context, DishDetailActivity.class);
        intent.putExtra("dishId", dish.getId());
        int userId = ((HomeActivity) context).getUserId(); // Lấy userId từ HomeActivity
        intent.putExtra("userId", userId);  // Truyền userId
        context.startActivity(intent);
    }
}

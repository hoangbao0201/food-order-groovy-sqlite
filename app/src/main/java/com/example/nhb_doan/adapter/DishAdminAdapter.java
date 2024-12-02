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

import com.example.nhb_doan.EditDishActivity;
import com.example.nhb_doan.R;
import com.example.nhb_doan.SQLiteHelper;
import com.example.nhb_doan.model.Dish;

import java.util.List;

public class DishAdminAdapter extends BaseAdapter {
    private Context context;

    Button editButton, deleteButton, viewDetailButton;
    private List<Dish> dishes;
    private LayoutInflater inflater;
    private SQLiteHelper sqLiteHelper;

    public DishAdminAdapter(Context context, List<Dish> dishes) {
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
            convertView = inflater.inflate(R.layout.dish_admin_list_item, parent, false);
        }

        Dish dish = dishes.get(position);

        ImageView imageView = convertView.findViewById(R.id.imageViewDishAdmin);
        TextView textViewName = convertView.findViewById(R.id.textViewDishNameAdmin);
        TextView textViewDescription = convertView.findViewById(R.id.textViewDescriptionAdmin);

        editButton = convertView.findViewById(R.id.editButtonDishAdmin);
        deleteButton = convertView.findViewById(R.id.deleteButtonDishAdmin);

        textViewName.setText(dish.getName());
        textViewDescription.setText(dish.getDescription());
        imageView.setImageURI(Uri.parse(dish.getImageUri()));

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDish(dish);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDish(dish.getId());
            }
        });

        return convertView;
    }

    private void editDish(Dish dish) {
        Intent intent = new Intent(context, EditDishActivity.class);
        intent.putExtra("dishId", dish.getId());
        context.startActivity(intent);
    }

    private void deleteDish(final int dishId) {
        new AlertDialog.Builder(context)
                .setTitle("Xóa món ăn")
                .setMessage("Bạn có chắc chắn muốn xóa món ăn này?")
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        sqLiteHelper.deleteDish(dishId);

                        for (int i = 0; i < dishes.size(); i++) {
                            if (dishes.get(i).getId() == dishId) {
                                dishes.remove(i);
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

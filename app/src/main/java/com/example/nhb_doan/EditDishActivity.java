package com.example.nhb_doan;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nhb_doan.model.Dish;

public class EditDishActivity extends Activity {
    private EditText dishNameEditText, dishDescriptionEditText, dishQuantityEditText;
    private ImageView dishImageView;
    private Button saveDishButton, selectImageButton;

    private int dishId;
    private Uri selectedImageUri;
    private SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dish);

        // Ánh xạ các thành phần giao diện
        dishNameEditText = findViewById(R.id.dishName);
        dishDescriptionEditText = findViewById(R.id.dishDescription);
        dishImageView = findViewById(R.id.dishImageView);
        saveDishButton = findViewById(R.id.saveDishButton);
        selectImageButton = findViewById(R.id.selectImageButton);

        // Khởi tạo SQLiteHelper
        sqLiteHelper = new SQLiteHelper(this);
        dishId = getIntent().getIntExtra("dishId", -1);

        Dish dish = sqLiteHelper.getDishById(dishId);

        if (dish.getImageUri() != null) {
            dishImageView.setImageURI(Uri.parse(dish.getImageUri()));
        }

        // Hiển thị dữ liệu món ăn hiện tại
        dishNameEditText.setText(dish.getName());
        dishDescriptionEditText.setText(dish.getDescription());

        // Chọn ảnh mới
        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

        // Lưu thông tin đã chỉnh sửa
        saveDishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            dishImageView.setImageURI(selectedImageUri);
        }
    }

    private void saveDish() {
        // Lấy dữ liệu từ các trường nhập
        String name = dishNameEditText.getText().toString();
        String description = dishDescriptionEditText.getText().toString();

        if (name.isEmpty() || description.isEmpty() || selectedImageUri == null) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cập nhật dữ liệu món ăn vào cơ sở dữ liệu
        sqLiteHelper.updateDish(dishId, name, description, selectedImageUri);

        Toast.makeText(this, "Dish updated successfully", Toast.LENGTH_SHORT).show();
        finish(); // Quay lại màn hình trước
    }
}

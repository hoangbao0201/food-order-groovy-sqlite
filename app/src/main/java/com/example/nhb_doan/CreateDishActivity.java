package com.example.nhb_doan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.Manifest; // Đảm bảo dòng này có mặt


public class CreateDishActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private ImageView dishImageView;
    private EditText dishNameEditText, dishDescriptionEditText;
    private Button selectImageButton, saveDishButton;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_dish);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Nếu chưa được cấp quyền, yêu cầu quyền
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        dishImageView = findViewById(R.id.dishImageView);
        dishNameEditText = findViewById(R.id.dishName);
        dishDescriptionEditText = findViewById(R.id.dishDescription);
        selectImageButton = findViewById(R.id.selectImageButton);
        saveDishButton = findViewById(R.id.saveDishButton);

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mở thư viện ảnh để người dùng chọn hình ảnh món ăn
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });

        saveDishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dishName = dishNameEditText.getText().toString();
                String dishDescription = dishDescriptionEditText.getText().toString();

                if (dishName.isEmpty() || dishDescription.isEmpty() || selectedImageUri == null) {
                    Toast.makeText(CreateDishActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Gọi SQLiteHelper để lưu món ăn
                SQLiteHelper sqLiteHelper = new SQLiteHelper(CreateDishActivity.this);
                sqLiteHelper.addDish(CreateDishActivity.this, dishName, dishDescription, selectedImageUri);

                Toast.makeText(CreateDishActivity.this, "Dish added successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Quyền đã được cấp, bạn có thể thực hiện các thao tác liên quan đến bộ nhớ
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            } else {
                // Quyền bị từ chối, thông báo cho người dùng
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            dishImageView.setImageURI(selectedImageUri);
        }
    }
}
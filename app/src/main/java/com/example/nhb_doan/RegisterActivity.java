package com.example.nhb_doan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    EditText registerUsernameEditText, registerPasswordEditText;
    Button registerButton;
    SQLiteHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerUsernameEditText = findViewById(R.id.registerUsername);
        registerPasswordEditText = findViewById(R.id.registerPassword);
        registerButton = findViewById(R.id.registerButton);

        dbHelper = new SQLiteHelper(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = registerUsernameEditText.getText().toString();
                String password = registerPasswordEditText.getText().toString();

                // Kiểm tra nếu username không trống
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kiểm tra nếu username đã tồn tại trong cơ sở dữ liệu
                if (dbHelper.isUsernameExist(username)) {
                    Toast.makeText(RegisterActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                } else {
                    // Thêm người dùng mới vào cơ sở dữ liệu
                    dbHelper.addUser(username, password);
                    Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();

                    // Quay lại màn hình đăng nhập
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
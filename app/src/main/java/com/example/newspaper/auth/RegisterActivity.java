package com.example.newspaper.auth;

import android.app.Application;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.newspaper.R;
import com.example.newspaper.common.database.repositories.UserRepository;
import com.example.newspaper.common.models.User;
import com.example.newspaper.common.utils.EncryptionUtil;

public class RegisterActivity extends AppCompatActivity {

    private EditText edName, etEmail, etPassword;
    private Button btnRegister;
    private TextView linkToLogin;
    private ImageView backBtn;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        userRepository = new UserRepository((Application) getApplicationContext());
        findViewById(R.id.backBtn).setOnClickListener(v -> finish());
        mapping();

        Button btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(v -> {
            String fullName = edName.getText().toString();
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            User newUser = User.builder()
                    .email(email)
                    .fullName(fullName)
                    .role("USER")
                    .passwordHash(EncryptionUtil.hashPassword(password))
                    .build();

            userRepository.registerUser(newUser, new UserRepository.RegistrationCallback() {
                @Override
                public void onSuccess() {
                    runOnUiThread(() -> {
                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }

                @Override
                public void onError(String errorMessage) {
                    runOnUiThread(() -> {
                        Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    });
                }
            });
        });
    }

    public void mapping(){
        edName = findViewById(R.id.edName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
    }
}
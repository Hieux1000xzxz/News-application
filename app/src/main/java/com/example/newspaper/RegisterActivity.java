package com.example.newspaper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        navigate(R.id.linkToLogin, LoginActivity.class);
        navigate(R.id.backBtn, MainActivity.class);
    }

    public void navigate(int viewId, Class<?> targetActivity) {
        View button = findViewById(viewId);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(this, targetActivity);
            startActivity(intent);
        });
    }
}
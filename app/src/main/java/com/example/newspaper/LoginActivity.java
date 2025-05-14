package com.example.newspaper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        navigate(R.id.btnBack, MainActivity.class);
        navigate(R.id.linkToRegister, RegisterActivity.class);

        String userId = "123456";
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putString("userId", userId);
        editor.apply();

    }

    public void navigate(int viewId, Class<?> targetActivity) {
        View button = findViewById(viewId);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(this, targetActivity);
            startActivity(intent);
        });
    }
}
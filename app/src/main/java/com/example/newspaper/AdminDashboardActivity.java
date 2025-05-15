package com.example.newspaper;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.card.MaterialCardView;

public class AdminDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Setup click listeners for management cards
        setupCardClickListeners();
    }

    private void setupCardClickListeners() {
        // Quản lý chuyên mục
        MaterialCardView cardCategories = findViewById(R.id.cardCategories);
        cardCategories.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdminCategoryActivity.class);
            startActivity(intent);
        });

        // Quản lý bài viết
        MaterialCardView cardArticles = findViewById(R.id.cardArticles);
        cardArticles.setOnClickListener(v -> {
            // TODO: Implement article management
            // Intent intent = new Intent(this, AdminArticleActivity.class);
            // startActivity(intent);
        });

        // Quản lý người dùng
        MaterialCardView cardUsers = findViewById(R.id.cardUsers);
        cardUsers.setOnClickListener(v -> {
            // TODO: Implement user management
            // Intent intent = new Intent(this, AdminUserActivity.class);
            // startActivity(intent);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
} 
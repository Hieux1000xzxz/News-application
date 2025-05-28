package com.example.newspaper.user.activities;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newspaper.R;
import com.example.newspaper.common.models.Article;
import com.example.newspaper.common.pojo.ArticleWithCategory;
import com.example.newspaper.user.adapters.ArticleRecycleViewAdapter;
import com.example.newspaper.user.adapters.NotificationRecycleViewAdapter;
import com.example.newspaper.user.view_items.ArticleViewItem;
import com.example.newspaper.user.view_models.NotificationViewModel;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {
    private NotificationViewModel viewModel;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notification);

        viewModel = new ViewModelProvider(this).get(NotificationViewModel.class);

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = (int) prefs.getLong("userId", -1);
        recyclerView = findViewById(R.id.list_notification);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if(userId != -1){
            viewModel.getAllComment(userId).observe(this, notifications -> {
                NotificationRecycleViewAdapter adapter = new NotificationRecycleViewAdapter(notifications);
                recyclerView.setAdapter(adapter);
            });
        }

        findViewById(R.id.back_icon).setOnClickListener(v -> {
            finish();
        });
    }
}
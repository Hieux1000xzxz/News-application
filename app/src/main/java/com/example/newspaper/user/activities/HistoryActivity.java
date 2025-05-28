package com.example.newspaper.user.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newspaper.R;
import com.example.newspaper.user.adapters.ArticleRecycleViewAdapter;
import com.example.newspaper.user.view_items.ArticleViewItem;
import com.example.newspaper.user.view_models.ArticleViewModel;
import com.example.newspaper.user.view_models.ReadHistoryViewModel;
import com.example.newspaper.user.view_models.SavedArticleViewModel;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private SavedArticleViewModel savedArticleViewModel;
    private ReadHistoryViewModel readHistoryViewModel;
    private ArticleViewModel articleViewModel;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history);

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = (int) prefs.getLong("userId", -1);

        savedArticleViewModel = new ViewModelProvider(this).get(SavedArticleViewModel.class);
        articleViewModel = new ViewModelProvider(this).get(ArticleViewModel.class);
        readHistoryViewModel = new ViewModelProvider(this).get(ReadHistoryViewModel.class);

        recyclerView = findViewById(R.id.listViews);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.back_icon).setOnClickListener(v -> {
            finish();
        });

        TextView title = findViewById(R.id.title);
        String type = getIntent().getStringExtra("type");
        if(type.equals("history")){
            title.setText("Bài viết đã xem");

            readHistoryViewModel.getAllSavedArticle(userId).observe(this, articles -> {
                articleViewModel.loadSavedOrHistoryArticle(articles);
            });
        }
        else if(type.equals("saved")){
            title.setText("Bài viết đã lưu");

            savedArticleViewModel.getAllSavedArticle(userId).observe(this, articleIds -> {
                articleViewModel.loadSavedOrHistoryArticle(articleIds);
            });
        }

        articleViewModel.getSavedOrHistoryArticles().observe(this, articles -> {
            List<ArticleViewItem> items = new ArrayList<>();
            articles.forEach(a -> items.add(new ArticleViewItem(a, ArticleViewItem.TypeDisplay.SAVED_OR_HISTORY)));
            ArticleRecycleViewAdapter adapter = new ArticleRecycleViewAdapter(items);
            recyclerView.setAdapter(adapter);
        });
    }
}
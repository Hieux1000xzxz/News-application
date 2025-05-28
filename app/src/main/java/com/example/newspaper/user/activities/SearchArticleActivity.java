package com.example.newspaper.user.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newspaper.R;
import com.example.newspaper.common.database.repositories.ArticleRepository;
import com.example.newspaper.user.adapters.ArticleRecycleViewAdapter;
import com.example.newspaper.user.view_items.ArticleViewItem;
import com.example.newspaper.user.view_models.ArticleViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchArticleActivity extends AppCompatActivity {
    ArticleViewModel viewModel;
    TextView textKeyword;
    RecyclerView recyclerView;
    ArticleRepository repository;
    List<ArticleViewItem> items;
    ArticleRecycleViewAdapter adapter;
    String key1, key2, key3, key4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_article);

        mapping();

        String keyword = getIntent().getStringExtra("keyword");
        assert keyword != null;

        textKeyword.setText(keyword);
        String[] key = keyword.split(" ");
        key1 = key2 = key3 = key4 = key.length > 0 ? "%" + key[0] + "%" : "% %";

        if (key.length > 1) key2 = "%" + key[1] + "%";
        if (key.length > 2) key3 = "%" + key[2] + "%";
        if (key.length > 3) key4 = "%" + key[3] + "%";

        viewModel = new ViewModelProvider(this).get(ArticleViewModel.class);
        viewModel.loadSearchArticles(key1, key2, key3, key4);
        viewModel.getSearchArticles().observe(this, articles -> {
            Log.d("Count items: ", String.valueOf(articles.size()));
            articles.forEach(a -> {
                items.add(new ArticleViewItem(a, ArticleViewItem.TypeDisplay.MAIN));
                adapter.setArticles(items);
            });
        });
    }

    public void mapping(){
        items = new ArrayList<>();
        repository = new ArticleRepository(this);
        textKeyword = findViewById(R.id.keyword);
        recyclerView = findViewById(R.id.listSearchArticle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ArticleRecycleViewAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        findViewById(R.id.back_icon).setOnClickListener(v -> {
            finish();
        });
    }
}
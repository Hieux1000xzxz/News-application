package com.example.newspaper.user.activities;

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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NewsPointActivity extends AppCompatActivity {
    private ArticleViewModel articleViewModel;
    private TextView title, childTitle;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_news_point);

        articleViewModel = new ViewModelProvider(this).get(ArticleViewModel.class);

        findViewById(R.id.back_icon).setOnClickListener(v -> {
            finish();
        });
        mapping();

        String type = getIntent().getStringExtra("type");
        System.out.println(type);
        assert type != null;
        if(type.equals("newest")){
            title.setText("Tin mới nhất");
            childTitle.setText(String.format("TIN MỚI NHẤT TRONG NGÀY %s", LocalDate.now()));

            articleViewModel.loadNewestArticle();
            articleViewModel.getNewestArticle().observe(this, articles -> {
                List<ArticleViewItem> items = new ArrayList<>();
                articles.forEach(a -> items.add(new ArticleViewItem(a, ArticleViewItem.TypeDisplay.NEW_POINT)));
                ArticleRecycleViewAdapter adapter = new ArticleRecycleViewAdapter(items);
                recyclerView.setAdapter(adapter);
            });
        }else if(type.equals("new-point")){
            title.setText("Điểm tin");
            childTitle.setText(String.format("ĐIỂM TIN NỔI BẬT HÔM QUA %s", LocalDate.now().minusDays(1)));
        }else if(type.equals("category")){
            Integer id = getIntent().getIntExtra("id", 0);
            String name = getIntent().getStringExtra("name");
            title.setText(String.format("Thể loại: %s", name));
            articleViewModel.loadCategoryArticles(id);
            articleViewModel.getCategoryArticles().observe(this, articles -> {
                List<ArticleViewItem> items = new ArrayList<>();
                articles.forEach(a -> items.add(new ArticleViewItem(a, ArticleViewItem.TypeDisplay.NEW_POINT)));
                ArticleRecycleViewAdapter adapter = new ArticleRecycleViewAdapter(items);
                recyclerView.setAdapter(adapter);
            });
        }
    }

    public void mapping(){
        title = findViewById(R.id.title);
        childTitle = findViewById(R.id.childTitle);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
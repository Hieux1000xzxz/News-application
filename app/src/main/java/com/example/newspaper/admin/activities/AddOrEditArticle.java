package com.example.newspaper.admin.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.newspaper.R;
import com.example.newspaper.admin.view_models.ArticleManagerViewModel;
import com.example.newspaper.admin.view_models.CategoryManagerViewModel;
import com.example.newspaper.common.models.Article;
import com.example.newspaper.user.view_models.CategoryViewModel;
import com.example.newspaper.user.view_models.UserViewModel;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.richeditor.RichEditor;

public class AddOrEditArticle extends AppCompatActivity {
    ArticleManagerViewModel viewModel;
    CategoryManagerViewModel categoryViewModel;
    UserViewModel userViewModel;
    EditText edtTitle, edtThumbnail, edtSummary;
    AutoCompleteTextView edtCategory;
    RichEditor editorContent;
    Button btnSave;
    ImageView imgPreview;
    Article currentArticle;
    String title, summary, content, thumbnail;
    int category, articleId;
    List<String> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_or_edit_article);

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = (int)prefs.getLong("userId", -1);
        articleId = getIntent().getIntExtra("articleId", -1);

        viewModel = new ViewModelProvider(this).get(ArticleManagerViewModel.class);
        categoryViewModel = new  ViewModelProvider(this).get(CategoryManagerViewModel.class);
        userViewModel = new  ViewModelProvider(this).get(UserViewModel.class);

        mapping();

        if(articleId == -1){
            btnSave.setOnClickListener(v -> {
                getInfo();
                if (TextUtils.isEmpty(title) ||
                        TextUtils.isEmpty(thumbnail) || TextUtils.isEmpty(content)) {
                    Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                }else{
                    viewModel.insert(Article.builder()
                                    .title(title)
                                    .summary(summary)
                                    .content(content)
                                    .thumbnailUrl(thumbnail)
                                    .publishedAt(Instant.now())
                                    .userId(userId)
                                    .categoryId(category)
                            .build());
                    finish();
                }
            });
        }else{
            viewModel.getOneArticle(articleId).observe(this, article -> {
                currentArticle = article;
                edtTitle.setText(currentArticle.getTitle());
                edtThumbnail.setText(currentArticle.getThumbnailUrl());
                edtSummary.setText(currentArticle.getSummary());
                editorContent.setHtml(currentArticle.getContent());

                Glide.with(this)
                        .load(currentArticle.getThumbnailUrl())
                        .transform(new CenterCrop(), new RoundedCorners(20))
                        .into(imgPreview);

                if (!options.isEmpty() && currentArticle.getCategoryId() < options.size()) {
                    edtCategory.setText(options.get(currentArticle.getCategoryId() - 1), false);
                }
            });

            btnSave.setOnClickListener(v -> {
                getInfo();
                if (TextUtils.isEmpty(title) ||
                        TextUtils.isEmpty(thumbnail) || TextUtils.isEmpty(content)) {
                    Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                }else{
                    currentArticle.setTitle(title);
                    currentArticle.setThumbnailUrl(thumbnail);
                    currentArticle.setSummary(summary);
                    currentArticle.setContent(content);
                    currentArticle.setUpdatedAt(Instant.now());
                    currentArticle.setCategoryId(category);
                    showDeleteConfirmationDialog(currentArticle);
                }

            });
        }

        mapping();
    }

    public void getInfo(){
        title = edtTitle.getText().toString().trim();
        thumbnail = edtThumbnail.getText().toString().trim();
        summary = edtSummary.getText().toString().trim();
        content = editorContent.getHtml();
        category = getCategoryIdByName(edtCategory.getText().toString());
    }

    public void mapping(){
        edtTitle = findViewById(R.id.edtTitle);
        edtCategory = findViewById(R.id.edtCategory);
        edtThumbnail = findViewById(R.id.edtThumbnail);
        edtSummary = findViewById(R.id.edtSummary);
        editorContent = findViewById(R.id.editorContent);
        btnSave = findViewById(R.id.btnSaveArticle);
        imgPreview = findViewById(R.id.imgPreview);
        options = new ArrayList<>();

        setSelectOption();
        loadThumbnail();

        editorContent.setEditorHeight(200);
        editorContent.setPlaceholder("Nhập nội dung bài viết...");
    }

    private void showDeleteConfirmationDialog(Article article) {
        new AlertDialog.Builder(this)
                .setTitle("Cập nhật bài viết")
                .setMessage("Bạn có chắc chắn muốn cập nhật thông tin cho bài viết này?")
                .setPositiveButton("Cập nhật", (dialog, which) -> {
                    viewModel.update(article);
                    Toast.makeText(this, "Đã cập nhật thông tin!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    public void setSelectOption(){
        categoryViewModel.getAllCategories().observe(this, categories -> {
            categories.forEach(c -> options.add(c.getName()));
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, options);
            edtCategory.setAdapter(adapter);

            if (currentArticle != null && currentArticle.getCategoryId() < options.size()) {
                edtCategory.setText(options.get(currentArticle.getCategoryId() - 1), false);
            }
        });
    }

    public int getCategoryIdByName(String name){
        for(int i = 0; i < options.size(); i++){
            if(name.equals(options.get(i))) return i+1;
        }
        return 0;
    }

    public void loadThumbnail(){
        edtThumbnail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Glide.with(AddOrEditArticle.this)
                        .load(edtThumbnail.getText().toString())
                        .transform(new CenterCrop(), new RoundedCorners(20))
                        .into(imgPreview);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}
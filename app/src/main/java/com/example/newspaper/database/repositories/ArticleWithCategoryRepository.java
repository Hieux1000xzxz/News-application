package com.example.newspaper.database.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.newspaper.database.DatabaseHandler;
import com.example.newspaper.database.dao.ArticleWithCategoryDao;
import com.example.newspaper.pojo.ArticleWithCategory;

import java.util.List;

public class ArticleWithCategoryRepository {
    private ArticleWithCategoryDao articleWithCategoryDao;
    private LiveData<List<ArticleWithCategory>> allArticlesWithCategory;

    public ArticleWithCategoryRepository(Context context) {
        DatabaseHandler dbh = DatabaseHandler.getInstance(context);
        this.articleWithCategoryDao = dbh.getArticleWithCategoryDao();
        this.allArticlesWithCategory = articleWithCategoryDao.getAllArticlesWithCategory();
    }

    public LiveData<List<ArticleWithCategory>> getAllArticlesWithCategory() {
        return allArticlesWithCategory;
    }

    public LiveData<List<ArticleWithCategory>> getArticlesByCategory(String categoryName) {
        return articleWithCategoryDao.getArticlesByCategory(categoryName);
    }
}
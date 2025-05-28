package com.example.newspaper.common.database.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.newspaper.common.database.DatabaseHandler;
import com.example.newspaper.common.database.dao.SavedArticleDao;
import com.example.newspaper.common.models.SavedArticle;

import java.util.List;
import java.util.concurrent.Executors;

public class SavedArticleRepository {
    private SavedArticleDao savedArticleDao;

    public SavedArticleRepository(Context context) {
        DatabaseHandler dbh = DatabaseHandler.getInstance(context);

        this.savedArticleDao = dbh.getSavedArticleDao();
    }

    public void insert(SavedArticle savedArticle){
        Executors.newSingleThreadExecutor().execute(() -> {
            this.savedArticleDao.insert(savedArticle);
        });
    }

    public void delete(SavedArticle savedArticle){
        Executors.newSingleThreadExecutor().execute(() -> {
            this.savedArticleDao.delete(savedArticle);
        });
    }

    public LiveData<List<Integer>> getAllSavedArticle(int userId){
        return savedArticleDao.getAllSavedArticles(userId);
    }

    public LiveData<SavedArticle> getSavedArticle(int userId, int articleId){
        return savedArticleDao.getSavedArticle(userId, articleId);
    }
}

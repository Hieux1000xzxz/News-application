package com.example.newspaper.user.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.newspaper.common.database.repositories.SavedArticleRepository;
import com.example.newspaper.common.models.SavedArticle;

import java.util.List;

public class SavedArticleViewModel extends AndroidViewModel {
    private final SavedArticleRepository repository;
    private final MutableLiveData<List<Integer>> articles = new MutableLiveData<>();
    private final MutableLiveData<SavedArticle> savedArticle = new MutableLiveData<>();
    public SavedArticleViewModel(@NonNull Application application) {
        super(application);

        repository = new SavedArticleRepository(application);
    }

    public void insert(SavedArticle savedArticle){
        repository.insert(savedArticle);
    }

    public void delete(SavedArticle savedArticle){
        repository.delete(savedArticle);
    }

    public LiveData<List<Integer>> getAllSavedArticle(int userId){
        repository.getAllSavedArticle(userId).observeForever(articles::setValue);
        return articles;
    }

    public LiveData<SavedArticle> getSavedArticle(int userId, int articleId){
        repository.getSavedArticle(userId, articleId).observeForever(savedArticle::setValue);
        return savedArticle;
    }
}

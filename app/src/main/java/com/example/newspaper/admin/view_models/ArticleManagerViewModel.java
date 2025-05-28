package com.example.newspaper.admin.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.newspaper.common.database.repositories.ArticleRepository;
import com.example.newspaper.common.database.repositories.CategoryRepository;
import com.example.newspaper.common.models.Article;
import com.example.newspaper.common.models.Category;

import java.util.ArrayList;
import java.util.List;

public class ArticleManagerViewModel extends AndroidViewModel {

    private final ArticleRepository repository;
    private final MutableLiveData<List<Article>> articles = new MutableLiveData<>();
    private final MutableLiveData<Article> article = new MutableLiveData<>();

    public ArticleManagerViewModel(@NonNull Application application) {
        super(application);
        repository = new ArticleRepository(application);
    }

    public LiveData<List<Article>> getAllArticle() {
        repository.getAll().observeForever(articles::setValue);
        return articles;
    }

    public LiveData<Article> getOneArticle(int id) {
        repository.getOne(id).observeForever(article::setValue);
        return article;
    }

    public void insert(Article article) {
        repository.insert(article);
    }

    public void update(Article article) {
        repository.update(article);
    }

    public void delete(Article article) {
        repository.delete(article);
    }
}
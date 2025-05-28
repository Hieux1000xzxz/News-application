package com.example.newspaper.user.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.newspaper.common.database.repositories.ArticleRepository;
import com.example.newspaper.common.pojo.ArticleWithCategory;

import java.util.List;
import java.util.concurrent.Executors;

public class ArticleViewModel extends AndroidViewModel {
    private final ArticleRepository repository;
    private final MutableLiveData<List<ArticleWithCategory>> articles = new MutableLiveData<>();
    private final MutableLiveData<List<ArticleWithCategory>> searchArticles = new MutableLiveData<>();
    private final MutableLiveData<List<ArticleWithCategory>> categoryArticles = new MutableLiveData<>();
    private final MutableLiveData<List<ArticleWithCategory>> savedOrHistoryArticles = new MutableLiveData<>();
    private final MutableLiveData<List<ArticleWithCategory>> relatedArticles = new MutableLiveData<>();
    private final MutableLiveData<List<ArticleWithCategory>> newestArticle = new MutableLiveData<>();
    private final MutableLiveData<ArticleWithCategory> articleDetail = new MutableLiveData<>();

    public ArticleViewModel(@NonNull Application application) {
        super(application);
        this.repository = new ArticleRepository(application);
    }

    public LiveData<List<ArticleWithCategory>> getArticles() {
        return articles;
    }

    public MutableLiveData<List<ArticleWithCategory>> getSearchArticles() {
        return searchArticles;
    }

    public MutableLiveData<List<ArticleWithCategory>> getCategoryArticles() {
        return categoryArticles;
    }
    public LiveData<ArticleWithCategory> getArticle(){
        return articleDetail;
    }

    public MutableLiveData<List<ArticleWithCategory>> getSavedOrHistoryArticles() {
        return savedOrHistoryArticles;
    }

    public MutableLiveData<List<ArticleWithCategory>> getRelatedArticles() {
        return relatedArticles;
    }

    public MutableLiveData<List<ArticleWithCategory>> getNewestArticle() {
        return newestArticle;
    }

    public void loadArticles(int limit, int offset) {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<ArticleWithCategory> data = repository.getPagedArticles(limit, offset);
            articles.postValue(data);
        });
    }

    public void upViewCount(int id) {
        Executors.newSingleThreadExecutor().execute(() -> {
            repository.increaseViewCount(id);
        });
    }

    public void loadSearchArticles(String key1, String key2, String key3, String key4){
        Executors.newSingleThreadExecutor().execute(() -> {
            List<ArticleWithCategory> data = repository.getArticleByKeyword(key1, key2, key3, key4);
            searchArticles.postValue(data);
        });
    }

    public void loadCategoryArticles(int categoryId){
        Executors.newSingleThreadExecutor().execute(() -> {
            List<ArticleWithCategory> data = repository.getArticleByCategoryId(categoryId);
            categoryArticles.postValue(data);
        });
    }

    public void loadArticle(int id){
        Executors.newSingleThreadExecutor().execute(() -> {
            ArticleWithCategory data = repository.getArticleById(id);
            articleDetail.postValue(data);
        });
    }

    public void loadSavedOrHistoryArticle(List<Integer> ids){
        Executors.newSingleThreadExecutor().execute(() -> {
            List<ArticleWithCategory> data = repository.getArticleByIds(ids);
            savedOrHistoryArticles.postValue(data);
        });
    }

    public void loadRelatedArticle(int categoryId){
        Executors.newSingleThreadExecutor().execute(() -> {
            List<ArticleWithCategory> data = repository.getRelatedArticle(categoryId);
            relatedArticles.postValue(data);
        });
    }

    public void loadNewestArticle(){
        Executors.newSingleThreadExecutor().execute(() -> {
            List<ArticleWithCategory> data = repository.getArticleNewest();
            newestArticle.postValue(data);
        });
    }
}

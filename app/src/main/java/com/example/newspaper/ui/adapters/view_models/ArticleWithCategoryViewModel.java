package com.example.newspaper.ui.adapters.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.newspaper.database.repositories.ArticleWithCategoryRepository;
import com.example.newspaper.pojo.ArticleWithCategory;

import java.util.List;

public class ArticleWithCategoryViewModel extends AndroidViewModel {
    private ArticleWithCategoryRepository repository;
    private LiveData<List<ArticleWithCategory>> allArticlesWithCategory;
    private MutableLiveData<String> categoryFilter;
    private LiveData<List<ArticleWithCategory>> articlesByCategory;

    public ArticleWithCategoryViewModel(@NonNull Application application) {
        super(application);
        repository = new ArticleWithCategoryRepository(application);
        allArticlesWithCategory = repository.getAllArticlesWithCategory();
        
        // Khởi tạo categoryFilter với giá trị mặc định là "all"
        categoryFilter = new MutableLiveData<>("all");
        
        // Tạo một LiveData phản ứng với sự thay đổi categoryFilter
        articlesByCategory = Transformations.switchMap(categoryFilter, input -> {
            if (input == null || input.equals("all") || input.isEmpty()) {
                return allArticlesWithCategory;
            } else {
                return repository.getArticlesByCategory(input);
            }
        });
    }

    public LiveData<List<ArticleWithCategory>> getAllArticlesWithCategory() {
        return allArticlesWithCategory;
    }

    public LiveData<List<ArticleWithCategory>> getArticlesByCategory() {
        return articlesByCategory;
    }

    public void setCategoryFilter(String category) {
        categoryFilter.setValue(category);
    }
}
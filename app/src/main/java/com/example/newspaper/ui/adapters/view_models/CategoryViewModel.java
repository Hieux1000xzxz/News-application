package com.example.newspaper.ui.adapters.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.newspaper.database.repositories.CategoryRepository;
import com.example.newspaper.models.Category;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {
    private CategoryRepository repository;
    private LiveData<List<Category>> allCategories;
    private LiveData<List<Category>> allCategoriesById;
    public CategoryViewModel(@NonNull Application application) {
        super(application);
        repository = new CategoryRepository(application);
        allCategories = repository.getAllCategories();
        allCategoriesById = repository.getAllCategoriesById();
    }

    public LiveData<List<Category>> getAllCategories() {
        return allCategories;
    }

    public LiveData<Category> getCategoryById(int id) {
        return repository.getCategoryById(id);
    }

    public LiveData<Category> getCategoryByName(String name) {
        return repository.getCategoryByName(name);
    }
    public LiveData<List<Category>> getAllCategoriesById() {
        return allCategoriesById;
    }


    public void insert(Category category) {
        repository.insert(category);
    }

    public void update(Category category) {
        repository.update(category);
    }

    public void delete(Category category) {
        repository.delete(category);
    }

    public void deleteAll() {
        repository.deleteAll();
    }
} 
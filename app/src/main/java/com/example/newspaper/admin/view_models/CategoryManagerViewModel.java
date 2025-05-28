package com.example.newspaper.admin.view_models;
import android.app.Application;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newspaper.R;
import com.example.newspaper.common.database.repositories.CategoryRepository;
import com.example.newspaper.common.models.Category;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class CategoryManagerViewModel extends AndroidViewModel {
    private CategoryRepository repository;
    private LiveData<List<Category>> allCategories;
    private LiveData<List<Category>> allCategoriesById;
    public CategoryManagerViewModel(@NonNull Application application) {
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

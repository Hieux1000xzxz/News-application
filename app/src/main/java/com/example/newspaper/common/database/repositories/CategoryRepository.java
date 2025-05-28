package com.example.newspaper.common.database.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.newspaper.common.database.DatabaseHandler;
import com.example.newspaper.common.database.dao.CategoryDao;
import com.example.newspaper.common.models.Category;

import java.util.List;
import java.util.concurrent.Executors;

public class CategoryRepository {

    private CategoryDao categoryDao;
    private LiveData<List<Category>> allCategories;
    private LiveData<List<Category>> allCategoriesById;
    public CategoryRepository(Context context){
        DatabaseHandler dbh = DatabaseHandler.getInstance(context);
        this.categoryDao = dbh.getCategoryDao();
        this.allCategories = categoryDao.getAllCategories();
        this.allCategoriesById = categoryDao.getAllCategoriesById();
    }

    public void insert(Category category) {
        Executors.newSingleThreadExecutor().execute(() -> {
            this.categoryDao.insert(category);
        });
    }

    public void update(Category category) {
        Executors.newSingleThreadExecutor().execute(() -> {
            this.categoryDao.update(category);
        });
    }

    public void delete(Category category) {
        Executors.newSingleThreadExecutor().execute(() -> {
            this.categoryDao.delete(category);
        });
    }

    public void deleteAll() {
        Executors.newSingleThreadExecutor().execute(() -> {
            this.categoryDao.deleteAlls();
        });
    }

    public LiveData<List<Category>> getAllCategory(){
        return categoryDao.findAlls();
    }

    public LiveData<List<Category>> getAllCategories() {
        return this.allCategories;
    }

    public LiveData<Category> getCategoryById(int id) {
        return this.categoryDao.getCategoryById(id);
    }

    public LiveData<Category> getCategoryByName(String name) {
        return this.categoryDao.getCategoryByName(name);
    }
    public Category getCategoryByNameSync(String name) {
        return categoryDao.getCategoryByNameSync(name);
    }
    public LiveData<List<Category>> getAllCategoriesById(){
        return this.allCategoriesById;
    }
}

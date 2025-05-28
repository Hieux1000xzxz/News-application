package com.example.newspaper.common.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.newspaper.common.models.Category;

import java.util.List;

@Dao
public interface CategoryDao {
    @Insert
    void insert(Category category);

    @Update
    void update(Category category);

    @Delete
    void delete(Category category);

    @Query("DELETE FROM category_table")
    void deleteAlls();

    @Query("SELECT * FROM category_table ORDER BY id ASC")
    LiveData<List<Category>> findAlls();

    @Query("SELECT * FROM category_table ORDER BY id ASC")
    List<Category> getAllDirect();

    @Query("SELECT * FROM category_table ORDER BY id ASC")
    LiveData<List<Category>> getAllCategories();

    @Query("SELECT * FROM category_table WHERE id = :id")
    LiveData<Category> getCategoryById(int id);

    @Query("SELECT * FROM category_table WHERE name = :name")
    LiveData<Category> getCategoryByName(String name);

    @Query("SELECT * FROM category_table WHERE name = :name LIMIT 1")
    Category getCategoryByNameSync(String name);

    @Query("SELECT * FROM category_table ORDER BY id ASC")
    LiveData<List<Category>> getAllCategoriesById();
}

package com.example.newspaper.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.newspaper.pojo.ArticleWithCategory;

import java.util.List;

@Dao
public interface ArticleWithCategoryDao {

    @Transaction
    @Query("SELECT article_table.*, category_table.* FROM article_table " +
           "INNER JOIN category_table ON article_table.categoryId = category_table.id " +
           "ORDER BY article_table.publishedAt DESC")
    LiveData<List<ArticleWithCategory>> getAllArticlesWithCategory();

    @Transaction
    @Query("SELECT article_table.*, category_table.* FROM article_table " +
           "INNER JOIN category_table ON article_table.categoryId = category_table.id " +
           "WHERE category_table.name = :categoryName " +
           "ORDER BY article_table.publishedAt DESC")
    LiveData<List<ArticleWithCategory>> getArticlesByCategory(String categoryName);
}
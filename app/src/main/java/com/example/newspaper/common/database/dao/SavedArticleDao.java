package com.example.newspaper.common.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.newspaper.common.models.SavedArticle;

import java.util.List;

@Dao
public interface SavedArticleDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(SavedArticle article);

    @Delete
    void delete(SavedArticle article);

    @Query("SELECT articleId FROM saved_articles_table WHERE userId = :userId")
    LiveData<List<Integer>> getAllSavedArticles(int userId);

    @Query("DELETE FROM saved_articles_table WHERE userId = :userId")
    void deleteByArticleId(int userId);

    @Query("SELECT * FROM saved_articles_table WHERE userId = :userId AND articleId = :articleId")
    LiveData<SavedArticle> getSavedArticle(int userId, int articleId);
}
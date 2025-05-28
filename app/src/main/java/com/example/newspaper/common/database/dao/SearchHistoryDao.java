package com.example.newspaper.common.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.newspaper.common.models.SearchHistory;

import java.util.List;

@Dao
public interface SearchHistoryDao {
    @Insert
    void insert(SearchHistory SearchHistory);

    @Update
    void update(SearchHistory SearchHistory);

    @Delete
    void delete(SearchHistory SearchHistory);

    @Query("DELETE FROM search_history_table WHERE :userId")
    void deleteByUserId(int userId);

    @Query("DELETE FROM search_history_table")
    void deleteAlls();

    @Query("SELECT * FROM search_history_table WHERE userId = :userId ORDER BY id DESC")
    LiveData<List<SearchHistory>> getSearchHistoryByUserId(int userId);

    @Query("SELECT keyword FROM search_history_table GROUP BY keyword ORDER BY COUNT(*) DESC LIMIT 10")
    LiveData<List<String>> getSearchTrends();

}

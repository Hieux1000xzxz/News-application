package com.example.newspaper.common.database.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.newspaper.common.database.DatabaseHandler;
import com.example.newspaper.common.database.dao.SearchHistoryDao;
import com.example.newspaper.common.models.SearchHistory;

import java.util.List;
import java.util.concurrent.Executors;

public class SearchHistoryRepository {
    private SearchHistoryDao searchHistoryDao;

    public SearchHistoryRepository(Context context) {
        DatabaseHandler dbh = DatabaseHandler.getInstance(context);

        this.searchHistoryDao = dbh.getSearchHistoryDao();
    }

    public void insert(SearchHistory searchHistory) {
        Executors.newSingleThreadExecutor().execute(() -> {
            this.searchHistoryDao.insert(searchHistory);
        });
    }

    public void update(SearchHistory searchHistory) {
        Executors.newSingleThreadExecutor().execute(() -> {
            this.searchHistoryDao.update(searchHistory);
        });
    }

    public void delete(SearchHistory searchHistory) {
        Executors.newSingleThreadExecutor().execute(() -> {
            this.searchHistoryDao.delete(searchHistory);
        });
    }

    public void deleteByUserId(int userId) {
        Executors.newSingleThreadExecutor().execute(() -> {
            this.searchHistoryDao.deleteByUserId(userId);
        });
    }

    public void deleteAll() {
        Executors.newSingleThreadExecutor().execute(() -> {
            this.searchHistoryDao.deleteAlls();
        });
    }

    public LiveData<List<SearchHistory>> getSearchHistory(int userId) {

        return searchHistoryDao.getSearchHistoryByUserId(userId);
    }

    public LiveData<List<String>> getSearchTrends(){
        return searchHistoryDao.getSearchTrends();
    }
}

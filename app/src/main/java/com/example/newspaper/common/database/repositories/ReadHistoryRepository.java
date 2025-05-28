package com.example.newspaper.common.database.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.newspaper.common.database.DatabaseHandler;
import com.example.newspaper.common.database.dao.ReadHistoryDao;
import com.example.newspaper.common.models.ReadHistory;

import java.util.List;
import java.util.concurrent.Executors;

public class ReadHistoryRepository {

    private ReadHistoryDao readHistoryDao;


    public ReadHistoryRepository(Context context) {
        DatabaseHandler dbh = DatabaseHandler.getInstance(context);
        this.readHistoryDao = dbh.getReadHistoryDao();
    }

    public void insert(ReadHistory readHistory) {
        Executors.newSingleThreadExecutor().execute(() -> {
            this.readHistoryDao.insert(readHistory);
        });
    }

    public void delete(ReadHistory readHistory) {
        Executors.newSingleThreadExecutor().execute(() -> {
            this.readHistoryDao.delete(readHistory);
        });
    }

    public void deleteAll(int userId) {
        Executors.newSingleThreadExecutor().execute(() -> {
            this.readHistoryDao.deleteAll(userId);
        });
    }

    public LiveData<List<Integer>> getAlls(int userId) {
        return this.readHistoryDao.findAllByUserId(userId);
    }

    public LiveData<ReadHistory> getOne(int userId, int articleId){
        return this.readHistoryDao.getReadHistory(userId, articleId);
    }
}
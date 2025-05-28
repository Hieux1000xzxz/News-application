package com.example.newspaper.user.view_models;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.newspaper.common.database.repositories.SearchHistoryRepository;
import com.example.newspaper.common.models.SearchHistory;

import java.util.List;

public class SearchViewModel extends AndroidViewModel {
    private SearchHistoryRepository repository;
    public LiveData<List<SearchHistory>> histories;
    public LiveData<List<String>> trends;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        repository = new SearchHistoryRepository(application);
        SharedPreferences prefs = getApplication().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        int id = (int) prefs.getLong("userId", -1);
        histories = repository.getSearchHistory(id);
        trends = repository.getSearchTrends();
    }
}

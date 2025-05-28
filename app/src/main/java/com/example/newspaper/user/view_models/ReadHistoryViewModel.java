package com.example.newspaper.user.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.newspaper.common.database.repositories.ReadHistoryRepository;
import com.example.newspaper.common.models.ReadHistory;

import java.util.List;

public class ReadHistoryViewModel extends AndroidViewModel {
    private final ReadHistoryRepository repository;
    private final MutableLiveData<List<Integer>> articles = new MutableLiveData<>();
    private final MutableLiveData<ReadHistory> readHistory = new MutableLiveData<>();
    public ReadHistoryViewModel(@NonNull Application application) {
        super(application);

        repository = new ReadHistoryRepository(application);
    }

    public void insert(ReadHistory readHistory){
        repository.insert(readHistory);
    }

    public LiveData<List<Integer>> getAllSavedArticle(int userId){
        repository.getAlls(userId).observeForever(articles::setValue);
        return articles;
    }

    public LiveData<ReadHistory> getSavedArticle(int userId, int articleId){
        repository.getOne(userId, articleId).observeForever(readHistory::setValue);
        return readHistory;
    }
}

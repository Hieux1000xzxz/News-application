package com.example.newspaper.user.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.newspaper.common.database.repositories.NotificationRepository;
import com.example.newspaper.common.pojo.NotificationWithArticle;

import java.util.List;
import java.util.concurrent.Executors;

public class NotificationViewModel extends AndroidViewModel {
    private NotificationRepository repository;
    private final MutableLiveData<List<NotificationWithArticle>> notifications = new MutableLiveData<>();
    public NotificationViewModel(@NonNull Application application) {
        super(application);

        repository = new NotificationRepository(application);
    }

    public LiveData<List<NotificationWithArticle>> getAllComment(int userId) {
        repository.getNotifications(userId).observeForever(notifications::setValue);
        return notifications;
    }

    public void setIsRead(int id){
        Executors.newSingleThreadExecutor().execute(() -> {
            repository.setIsRead(id);
        });
    }
}

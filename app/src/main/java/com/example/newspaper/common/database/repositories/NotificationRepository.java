package com.example.newspaper.common.database.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.newspaper.common.database.DatabaseHandler;
import com.example.newspaper.common.database.dao.NotificationDao;
import com.example.newspaper.common.pojo.NotificationWithArticle;

import java.util.List;

public class NotificationRepository {
    private NotificationDao notificationDao;

    public NotificationRepository(Context context) {
        DatabaseHandler dbh = DatabaseHandler.getInstance(context);

        this.notificationDao = dbh.getNotificationDao();
    }

    public LiveData<List<NotificationWithArticle>> getNotifications(int userId){
        return notificationDao.getNotificationsWithArticles(userId);
    }

    public void setIsRead(int id){
        notificationDao.setIsRead(id);
    }
}

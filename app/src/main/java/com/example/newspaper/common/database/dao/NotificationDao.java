package com.example.newspaper.common.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.newspaper.common.models.Notification;
import com.example.newspaper.common.pojo.NotificationWithArticle;

import java.util.List;

@Dao
public interface NotificationDao {

    @Insert
    void insert(Notification notification);

    @Transaction
    @Query("SELECT * FROM notification_table WHERE userId = :userId ORDER BY createdAt DESC")
    LiveData<List<NotificationWithArticle>> getNotificationsWithArticles(int userId);

    @Query("UPDATE notification_table SET isRead = 1 WHERE id = :id")
    void setIsRead(int id);
}

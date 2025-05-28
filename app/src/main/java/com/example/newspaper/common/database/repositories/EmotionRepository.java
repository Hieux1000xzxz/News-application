package com.example.newspaper.common.database.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.newspaper.common.database.DatabaseHandler;
import com.example.newspaper.common.database.dao.EmotionDao;
import com.example.newspaper.common.models.Emotion;
import com.example.newspaper.common.pojo.EmotionCount;

import java.util.List;
import java.util.concurrent.Executors;

public class EmotionRepository {
    private EmotionDao emotionDao;

    public EmotionRepository(Context context) {
        DatabaseHandler dbh = DatabaseHandler.getInstance(context);

        this.emotionDao = dbh.getEmotionDao();
    }

    public LiveData<List<EmotionCount>> getEmotionCountsByArticleId(int articleId) {
        return emotionDao.getEmotionCountsByArticleId(articleId);
    }

    public LiveData<Emotion> getEmotionTypeByUserIdAndArticleId(int userId, int articleId) {
        return emotionDao.getEmotionTypeByUserIdAndArticleId(userId, articleId);
    }

    public void insert(Emotion emotion){
        Executors.newSingleThreadExecutor().execute(() -> {
            emotionDao.insert(emotion);
        });
    }

    public void update(Emotion emotion){
        Executors.newSingleThreadExecutor().execute(() -> {
            emotionDao.update(emotion);
        });
    }

    public void deleteAll(){
        Executors.newSingleThreadExecutor().execute(() -> {
            emotionDao.deleteAll();
        });
    }
}

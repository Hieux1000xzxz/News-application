package com.example.newspaper.common.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.newspaper.common.models.Emotion;
import com.example.newspaper.common.pojo.EmotionCount;

import java.util.List;

@Dao
public interface EmotionDao {
    @Query("SELECT type, COUNT(*) as count FROM emotion_table WHERE articleId = :articleId GROUP BY type")
    LiveData<List<EmotionCount>> getEmotionCountsByArticleId(int articleId);

    @Query("SELECT * FROM emotion_table WHERE userId = :userId AND articleId = :articleId")
    LiveData<Emotion> getEmotionTypeByUserIdAndArticleId(int userId, int articleId);

    @Insert
    void insert(Emotion emotion);

    @Update
    void update(Emotion emotion);

    @Query("DELETE FROM emotion_table")
    void deleteAll();
}

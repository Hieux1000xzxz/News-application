package com.example.newspaper.user.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.newspaper.common.database.repositories.EmotionRepository;
import com.example.newspaper.common.models.Emotion;
import com.example.newspaper.common.pojo.EmotionCount;

import java.util.List;

public class EmotionViewModel extends AndroidViewModel {
    private final EmotionRepository repository;
    private final MutableLiveData<List<EmotionCount>> emotionCounts = new MutableLiveData<>();
    private final MutableLiveData<Emotion> currentUserEmotion = new MutableLiveData<>();

    public EmotionViewModel(@NonNull Application application) {
        super(application);
        repository = new EmotionRepository(application);
    }

    public LiveData<List<EmotionCount>> getEmotionCounts(int articleId) {
        repository.getEmotionCountsByArticleId(articleId).observeForever(emotionCounts::setValue);
        return emotionCounts;
    }

    public LiveData<Emotion> getEmotionOfCurrentUser(int userId, int articleId) {
        repository.getEmotionTypeByUserIdAndArticleId(userId, articleId).observeForever(currentUserEmotion::setValue);
        return currentUserEmotion;
    }

    public void insertEmotion(Emotion emotion) {
        repository.insert(emotion);
    }

    public void updateEmotion(Emotion emotion) {
        repository.update(emotion);
    }

    public void deleteAll(){
        repository.deleteAll();
    }
}

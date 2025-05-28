package com.example.newspaper.user.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.newspaper.common.database.repositories.CommentRepository;
import com.example.newspaper.common.models.Comment;
import com.example.newspaper.common.pojo.CommentWithUser;

import java.util.List;

public class CommentViewModel extends AndroidViewModel {
    private CommentRepository repository;
    private final MutableLiveData<List<CommentWithUser>> comments = new MutableLiveData<>();

    private final MutableLiveData<List<CommentWithUser>> allComments = new MutableLiveData<>();
    private final MutableLiveData<Integer> count = new MutableLiveData<>();
    public CommentViewModel(@NonNull Application application) {
        super(application);
        repository = new CommentRepository(application);
    }

    public LiveData<List<CommentWithUser>> getComment(int articleId) {
        repository.getComments(articleId).observeForever(comments::setValue);
        return comments;
    }

    public LiveData<List<CommentWithUser>> getAllComment(int articleId) {
        repository.getAllComments(articleId).observeForever(allComments::setValue);
        return allComments;
    }

    public LiveData<Integer> count(int articleId){
        repository.countComment(articleId).observeForever(count::setValue);
        return count;
    }

    public void insert(Comment comment){
        repository.insert(comment);
    }
}

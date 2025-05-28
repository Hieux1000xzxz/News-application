package com.example.newspaper.common.database.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.newspaper.common.database.DatabaseHandler;
import com.example.newspaper.common.database.dao.CommentDao;
import com.example.newspaper.common.models.Comment;
import com.example.newspaper.common.pojo.CommentWithUser;

import java.util.List;
import java.util.concurrent.Executors;

public class CommentRepository {
    private CommentDao commentDao;

    public CommentRepository(Context context) {
        DatabaseHandler dbh = DatabaseHandler.getInstance(context);

        this.commentDao = dbh.getCommentDao();
    }

    public LiveData<List<CommentWithUser>> getComments(int id){
        return commentDao.getComments(id);
    }

    public LiveData<List<CommentWithUser>> getAllComments(int id){
        return commentDao.getAllComment(id);
    }

    public void insert(Comment comment){
        Executors.newSingleThreadExecutor().execute(() -> {
            commentDao.insert(comment);
        });
    }

    public LiveData<Integer> countComment(int articleId){
        return commentDao.count(articleId);
    };
}

package com.example.newspaper.common.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.newspaper.common.models.Comment;
import com.example.newspaper.common.pojo.CommentWithUser;

import java.util.List;

@Dao
public interface CommentDao {

    @Insert
    void insert(Comment comment);

    @Query("SELECT * FROM comment_table WHERE articleId = :id ORDER BY createdAt DESC LIMIT 4")
    LiveData<List<CommentWithUser>> getComments(int id);

    @Query("SELECT * FROM comment_table WHERE articleId = :id ORDER BY createdAt DESC")
    LiveData<List<CommentWithUser>> getAllComment(int id);

    @Query("SELECT COUNT(*) FROM comment_table WHERE articleId = :articleId")
    LiveData<Integer> count(int articleId);
}

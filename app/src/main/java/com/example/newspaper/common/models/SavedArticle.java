package com.example.newspaper.common.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.time.Instant;

import lombok.Builder;

@Builder
@Entity(
        tableName = "saved_articles_table",
        foreignKeys = {
                @ForeignKey(
                        entity = Article.class,
                        parentColumns = "id",
                        childColumns = "articleId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = User.class,
                        parentColumns = "id",
                        childColumns = "userId",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {@Index("articleId"), @Index("userId")}
)
public class SavedArticle {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int articleId;
    private int userId;
    private Instant savedAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Instant getSavedAt() {
        return savedAt;
    }

    public void setSavedAt(Instant savedAt) {
        this.savedAt = savedAt;
    }

    public SavedArticle() {
    }

    public SavedArticle(int id, int articleId, int userId, Instant savedAt) {
        this.id = id;
        this.articleId = articleId;
        this.userId = userId;
        this.savedAt = savedAt;
    }
}
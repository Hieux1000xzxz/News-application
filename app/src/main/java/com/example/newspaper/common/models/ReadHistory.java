package com.example.newspaper.common.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.time.Instant;

import lombok.Builder;

@Builder
@Entity(
        tableName = "read_history_table",
        foreignKeys = {
                @ForeignKey(
                        entity = User.class,
                        parentColumns = "id",
                        childColumns = "userId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Article.class,
                        parentColumns = "id",
                        childColumns = "articleId",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {@Index(value = {"userId", "articleId"}, unique = true)}
)
public class ReadHistory {
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private Integer userId;
    private Integer articleId;
    private Instant readAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public Instant getReadAt() {
        return readAt;
    }

    public void setReadAt(Instant readAt) {
        this.readAt = readAt;
    }

    public ReadHistory() {
    }

    @Ignore
    public ReadHistory(Integer id, Integer userId, Integer articleId, Instant readAt) {
        this.id = id;
        this.userId = userId;
        this.articleId = articleId;
        this.readAt = readAt;
    }
}

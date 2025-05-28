package com.example.newspaper.common.pojo;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.newspaper.common.models.Article;
import com.example.newspaper.common.models.Notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationWithArticle {
    @Embedded
    public Notification notification;

    @Relation(
            parentColumn = "articleId",
            entityColumn = "id"
    )
    public Article article;
}


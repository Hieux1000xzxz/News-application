package com.example.newspaper.common.pojo;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.newspaper.common.models.Comment;
import com.example.newspaper.common.models.User;

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
public class CommentWithUser {
    @Embedded
    public Comment comment;

    @Relation(
            parentColumn = "userId",
            entityColumn = "id"
    )
    public User user;

}

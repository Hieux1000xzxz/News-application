package com.example.newspaper.user.view_holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.newspaper.R;
import com.example.newspaper.common.pojo.CommentWithUser;
import com.example.newspaper.common.utils.DateTimeFormatterUtil;

public class CommentViewHolder extends BaseViewHolder<CommentWithUser> {
    ImageView avatar;
    TextView name, time, content;
    public CommentViewHolder(@NonNull View itemView) {
        super(itemView);

        avatar = findViewById(R.id.imgAvatar);
        name = findViewById(R.id.name);
        time = findViewById(R.id.comment_at);
        content = findViewById(R.id.content);
    }

    @Override
    public void onBindViewHolder(CommentWithUser item) {
        DateTimeFormatterUtil format = new DateTimeFormatterUtil();
        Glide.with(itemView.getContext())
                .load(item.user.avatarUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(avatar);

        name.setText(item.user.getFullName());
        time.setText(format.format(item.comment.getCreatedAt()));
        content.setText(item.comment.getContent());
    }
}

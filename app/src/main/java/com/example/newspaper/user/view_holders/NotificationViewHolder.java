package com.example.newspaper.user.view_holders;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.newspaper.common.pojo.NotificationWithArticle;
import com.example.newspaper.user.activities.ArticleDetailActivity;
import com.example.newspaper.R;
import com.example.newspaper.user.view_items.ArticleViewItem;
import com.example.newspaper.common.utils.DateTimeFormatterUtil;

public class NotificationViewHolder extends BaseViewHolder<NotificationWithArticle>{

    ImageView thumbnail;
    TextView title, publishedAt;
    View read;
    boolean isRead;
    int id, notificationId;
    public NotificationViewHolder(@NonNull View itemView) {
        super(itemView);

        thumbnail = findViewById(R.id.thumbnail);
        title = findViewById(R.id.title);
        publishedAt = findViewById(R.id.publish);
        read = findViewById(R.id.read);

        itemView.setOnClickListener(v -> {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Intent intent = new Intent(itemView.getContext(), ArticleDetailActivity.class);
                intent.putExtra("articleId", id);
                intent.putExtra("notificationId", notificationId);
                itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public void onBindViewHolder(NotificationWithArticle item) {
        DateTimeFormatterUtil formatter = new DateTimeFormatterUtil();

        publishedAt.setText(formatter.format(item.getArticle().getPublishedAt()));
        title.setText(item.getArticle().getTitle());
        Glide.with(itemView.getContext())
                .load(item.getArticle().getThumbnailUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(thumbnail);

        isRead = item.getNotification().getRead();
        id = item.getArticle().getId();
        notificationId = item.getNotification().getId();

        if(isRead){
            itemView.setAlpha(0.5f);
            read.setAlpha(0);
        }
    }
}

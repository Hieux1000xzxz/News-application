package com.example.newspaper.user.view_holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.newspaper.R;
import com.example.newspaper.user.view_items.ArticleViewItem;
import com.example.newspaper.common.utils.DateTimeFormatterUtil;

public class ArticleHistoryOrSavedViewHolder extends BaseViewHolder<ArticleViewItem> {

    TextView category, title, time;
    ImageView thumbnail;
    public ArticleHistoryOrSavedViewHolder(@NonNull View itemView) {
        super(itemView);

        category = findViewById(R.id.category);
        title = findViewById(R.id.title);
        time = findViewById(R.id.saved_or_read_at);
        thumbnail = findViewById(R.id.thumbnail);
    }

    @Override
    public void onBindViewHolder(ArticleViewItem item) {
        DateTimeFormatterUtil format = new DateTimeFormatterUtil();
        category.setText(item.getArticle().category.getName());
        title.setText(item.getArticle().article.getTitle());
        time.setText(format.format(item.getArticle().article.getPublishedAt()));
        Glide.with(itemView.getContext())
                .load(item.getArticle().article.getThumbnailUrl())
                .transform(new CenterCrop(), new RoundedCorners(15))
                .into(thumbnail);
    }
}

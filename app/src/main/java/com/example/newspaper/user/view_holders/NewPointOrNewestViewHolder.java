package com.example.newspaper.user.view_holders;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.newspaper.R;
import com.example.newspaper.user.activities.ArticleDetailActivity;
import com.example.newspaper.user.view_items.ArticleViewItem;
import com.example.newspaper.common.utils.DateTimeFormatterUtil;

public class NewPointOrNewestViewHolder extends BaseViewHolder<ArticleViewItem> {
    private final TextView title, category, time, summary;
    private final ImageView thumbnail;
    private int id;
    public NewPointOrNewestViewHolder(@NonNull View itemView) {
        super(itemView);

        title = findViewById(R.id.title);
        category = findViewById(R.id.category);
        time = findViewById(R.id.time);
        summary = findViewById(R.id.sumary);
        thumbnail = findViewById(R.id.thumbnail);

        itemView.setOnClickListener(v -> {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Intent intent = new Intent(itemView.getContext(), ArticleDetailActivity.class);
                intent.putExtra("articleId", id);
                itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public void onBindViewHolder(ArticleViewItem item) {
        DateTimeFormatterUtil format = new DateTimeFormatterUtil();
        title.setText(item.getArticle().article.getTitle());
        category.setText(item.getArticle().category.getName());
        time.setText(format.format(item.getArticle().article.getPublishedAt()));
        summary.setText(item.getArticle().article.getSummary());

        Glide.with(itemView.getContext())
                .load(item.getArticle().article.getThumbnailUrl())
                .transform(new CenterCrop(), new RoundedCorners(15))
                .into(thumbnail);

        id = item.getArticle().article.getId();
    }
}

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
import com.example.newspaper.user.activities.ArticleDetailActivity;
import com.example.newspaper.R;
import com.example.newspaper.user.view_items.ArticleViewItem;;

public class ArticleViewHolder extends BaseViewHolder<ArticleViewItem>{
    private ImageView thumbnail;
    private TextView title;
    private TextView description;
    private int id;
    public ArticleViewHolder(@NonNull View itemView) {
        super(itemView);

        thumbnail = findViewById(R.id.item_image);
        title = findViewById(R.id.item_title);
        description = findViewById(R.id.item_description);

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
        title.setText(item.getArticle().article.getTitle());
        description.setText(item.getArticle().article.getSummary());
        Glide.with(itemView.getContext())
                .load(item.getArticle().article.getThumbnailUrl())
                .transform(new CenterCrop(), new RoundedCorners(20))
                .into(thumbnail);
        id = item.getArticle().article.getId();
    }
}

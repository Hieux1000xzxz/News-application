package com.example.newspaper.user.view_holders;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newspaper.R;
import com.example.newspaper.user.activities.ArticleDetailActivity;
import com.example.newspaper.user.view_items.ArticleViewItem;
import com.example.newspaper.common.utils.DateTimeFormatterUtil;

public class ArticleCategoryViewHolder extends BaseViewHolder<ArticleViewItem>{
    private TextView timeText;
    private TextView categoryText;
    private TextView titleText;
    private TextView descriptionText;
    private View categoryDot;
    private int id;

    public ArticleCategoryViewHolder(@NonNull View itemView) {
        super(itemView);

        timeText = itemView.findViewById(R.id.timeText);
        categoryText = itemView.findViewById(R.id.categoryText);
        titleText = itemView.findViewById(R.id.titleText);
        descriptionText = itemView.findViewById(R.id.descriptionText);
        categoryDot = itemView.findViewById(R.id.categoryDot);
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
        DateTimeFormatterUtil formatter = new DateTimeFormatterUtil();

        timeText.setText(formatter.format(item.getArticle().article.getPublishedAt()));
        categoryText.setText(item.getArticle().category.getName());
        titleText.setText(item.getArticle().article.getTitle());
        descriptionText.setText(item.getArticle().article.getSummary());
        categoryDot.setBackgroundResource(R.drawable.circle_green);
        id = item.getArticle().article.getId();
    }
}

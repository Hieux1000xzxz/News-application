package com.example.newspaper.user.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.newspaper.R;
import com.example.newspaper.common.pojo.NotificationWithArticle;
import com.example.newspaper.user.view_holders.ArticleCategoryViewHolder;
import com.example.newspaper.user.view_holders.ArticleHistoryOrSavedViewHolder;
import com.example.newspaper.user.view_holders.ArticleRelatedViewHolder;
import com.example.newspaper.user.view_holders.ArticleViewHolder;
import com.example.newspaper.user.view_holders.BaseViewHolder;
import com.example.newspaper.user.view_holders.NewPointOrNewestViewHolder;
import com.example.newspaper.user.view_holders.NotificationViewHolder;
import com.example.newspaper.user.view_items.ArticleViewItem;

import java.util.List;

public class NotificationRecycleViewAdapter extends BaseRecycleViewAdapter<NotificationWithArticle> {
    private List<NotificationWithArticle> items;

    public NotificationRecycleViewAdapter(List<NotificationWithArticle> items) {
        super(items);
        this.items = items;
    }

    public void setArticles(List<NotificationWithArticle> articles) {
        this.items = articles;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new NotificationViewHolder(inflater.inflate(R.layout.item_notification_view, parent, false));
        }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBindViewHolder(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }
}

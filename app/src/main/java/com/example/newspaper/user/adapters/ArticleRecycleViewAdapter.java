package com.example.newspaper.user.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newspaper.R;
import com.example.newspaper.user.view_holders.ArticleCategoryViewHolder;
import com.example.newspaper.user.view_holders.ArticleRelatedViewHolder;
import com.example.newspaper.user.view_holders.ArticleViewHolder;
import com.example.newspaper.user.view_holders.ArticleHistoryOrSavedViewHolder;
import com.example.newspaper.user.view_holders.BaseViewHolder;
import com.example.newspaper.user.view_holders.NewPointOrNewestViewHolder;
import com.example.newspaper.user.view_holders.NotificationViewHolder;
import com.example.newspaper.user.view_items.ArticleViewItem;

import java.util.List;

public class ArticleRecycleViewAdapter extends BaseRecycleViewAdapter<ArticleViewItem> {
    private List<ArticleViewItem> items;
    private boolean isLoading = false;
    int page = 1;

    public ArticleRecycleViewAdapter(List<ArticleViewItem> items) {
        super(items);
        this.items = items;
    }

    public void setArticles(List<ArticleViewItem> articles) {
        this.items = articles;
        notifyDataSetChanged();
    }
    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType().ordinal();
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ArticleViewItem.TypeDisplay type = ArticleViewItem.TypeDisplay.values()[viewType];

        switch (type) {
            case MAIN:
                return new ArticleViewHolder(inflater.inflate(R.layout.item_blog_view, parent, false));
            case CATEGORY:
                return new ArticleCategoryViewHolder(inflater.inflate(R.layout.item_article_category_view, parent, false));
            case NOTIFICATION:
                return new NotificationViewHolder(inflater.inflate(R.layout.item_notification_view, parent, false));
            case SAVED_OR_HISTORY:
                return new ArticleHistoryOrSavedViewHolder(inflater.inflate(R.layout.item_history_or_saved_view, parent, false));
            case RELATED:
                return new ArticleRelatedViewHolder(inflater.inflate(R.layout.item_related_acticle_view, parent, false));
            case NEW_POINT:
                return new NewPointOrNewestViewHolder(inflater.inflate(R.layout.item_news_point_view, parent, false));
            default:
                throw new IllegalArgumentException("Unknown view type: " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBindViewHolder(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public void setLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    public void setOnScrollListener(RecyclerView recyclerView, final int pageSize, final int totalCount) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null) {
                    int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                    if (!isLoading && lastVisibleItemPosition == getItemCount() - 1 && getItemCount() < totalCount) {
                        loadNextPage(pageSize);
                    }
                }
            }
        });
    }

    private void loadNextPage(int pageSize) {

    }
}

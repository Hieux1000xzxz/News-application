package com.example.newspaper.user.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.newspaper.R;
import com.example.newspaper.common.models.SearchHistory;
import com.example.newspaper.user.view_holders.BaseViewHolder;
import com.example.newspaper.user.view_holders.SearchHistoryViewHolder;

import java.util.List;

public class SearchHistoryAdapter extends BaseRecycleViewAdapter<SearchHistory>{
    private List<SearchHistory> items;

    public SearchHistoryAdapter(List<SearchHistory> items) {
        super(items);
        this.items = items;
    }

    public void setArticles(List<SearchHistory> articles) {
        this.items = articles;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new SearchHistoryViewHolder(inflater.inflate(R.layout.item_history_search_view, parent, false));

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

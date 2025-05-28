package com.example.newspaper.user.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.newspaper.R;
import com.example.newspaper.common.pojo.CommentWithUser;
import com.example.newspaper.user.view_holders.BaseViewHolder;
import com.example.newspaper.user.view_holders.CommentViewHolder;

import java.util.List;

public class CommentRecycleVIewAdapter extends BaseRecycleViewAdapter<CommentWithUser>{
    private List<CommentWithUser> items;

    public CommentRecycleVIewAdapter(List<CommentWithUser> items) {
        super(items);
        this.items = items;
    }

    public void setComments(List<CommentWithUser> articles) {
        this.items = articles;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new CommentViewHolder(inflater.inflate(R.layout.item_comment_view, parent, false));
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

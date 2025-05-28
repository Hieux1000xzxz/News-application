package com.example.newspaper.user.adapters;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newspaper.user.view_holders.BaseViewHolder;

import java.util.List;

public class BaseRecycleViewAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    private List<T> items;

    public BaseRecycleViewAdapter(List<T> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBindViewHolder(this.items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

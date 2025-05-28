package com.example.newspaper.user.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.newspaper.R;
import com.example.newspaper.common.models.Category;
import com.example.newspaper.user.view_holders.BaseViewHolder;
import com.example.newspaper.user.view_holders.CategoryViewHolder;

import java.util.List;

public class CategoryRecycleViewAdapter extends BaseRecycleViewAdapter<Category> {
    List<Category> categories;
    public CategoryRecycleViewAdapter(List<Category> items) {
        super(items);

        categories = items;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new CategoryViewHolder(inflater.inflate(R.layout.item_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBindViewHolder(categories.get(position));
    }

    @Override
    public int getItemCount() {
        return categories != null ? categories.size() : 0;
    }
}

package com.example.newspaper.user.view_holders;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {
    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public abstract void onBindViewHolder(T item);

    public <T extends View> T findViewById(@IdRes int id){
        return this.itemView.findViewById(id);
    }
}

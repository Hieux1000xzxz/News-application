package com.example.newspaper.admin.view_holder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newspaper.R;

public class ArticleManagerViewHolder extends RecyclerView.ViewHolder{
    public TextView tvTitle, tvStatus;
    public Button btnEdit, btnDelete;
    public ImageView imgThumbnail;

    public ArticleManagerViewHolder(@NonNull View itemView) {
        super(itemView);
        tvTitle = itemView.findViewById(R.id.tvTitle);
        tvStatus = itemView.findViewById(R.id.tvStatus);
        btnEdit = itemView.findViewById(R.id.btnEdit);
        btnDelete = itemView.findViewById(R.id.btnDelete);
        imgThumbnail = itemView.findViewById(R.id.imgThumbnail);
    }
}

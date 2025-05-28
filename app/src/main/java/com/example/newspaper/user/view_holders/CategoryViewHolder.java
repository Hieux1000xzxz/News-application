package com.example.newspaper.user.view_holders;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newspaper.user.activities.NewsPointActivity;
import com.example.newspaper.R;
import com.example.newspaper.common.models.Category;

public class CategoryViewHolder extends BaseViewHolder<Category> {
    TextView name;
    int id;
    String categoryName;
    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);

        name = findViewById(R.id.categoryName);
        itemView.setOnClickListener(v -> {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Intent intent = new Intent(itemView.getContext(), NewsPointActivity.class);
                intent.putExtra("type", "category");
                intent.putExtra("id", id);
                intent.putExtra("name", categoryName);
                itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public void onBindViewHolder(Category item) {
        name.setText(item.getName());
        id = item.getId();
        categoryName = item.getName();
    }
}

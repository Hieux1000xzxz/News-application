package com.example.newspaper.admin.view_holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newspaper.R;
import com.example.newspaper.admin.adapter.CategoryManagerAdapter;
import com.example.newspaper.common.models.Category;
import com.google.android.material.button.MaterialButton;

public class CategoryManagerViewHolder extends RecyclerView.ViewHolder{
    private final TextView categoryId;
    private final TextView categoryName;
    private final MaterialButton btnEdit;
    private final MaterialButton btnDelete;

    public CategoryManagerViewHolder(@NonNull View itemView,
                              CategoryManagerAdapter.OnCategoryEditListener editListener,
                              CategoryManagerAdapter.OnCategoryDeleteListener deleteListener) {
        super(itemView);
        categoryId = itemView.findViewById(R.id.categoryId);
        categoryName = itemView.findViewById(R.id.categoryName);
        btnEdit = itemView.findViewById(R.id.btnEdit);
        btnDelete = itemView.findViewById(R.id.btnDelete);

        btnEdit.setOnClickListener(v -> {
            if (editListener != null) {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    editListener.onEdit((Category) v.getTag());
                }
            }
        });

        btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    deleteListener.onDelete((Category) v.getTag());
                }
            }
        });
    }

    public void bind(Category category) {
        categoryId.setText(category.getId() != null ? category.getId().toString() : "");
        categoryName.setText(category.getName());
        btnEdit.setTag(category);
        btnDelete.setTag(category);
    }
}

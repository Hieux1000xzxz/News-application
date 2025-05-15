package com.example.newspaper.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newspaper.R;
import com.example.newspaper.models.Category;

import java.util.ArrayList;
import java.util.List;

public class AdminCategoryAdapter extends RecyclerView.Adapter<AdminCategoryAdapter.CategoryViewHolder> {
    private List<Category> categories = new ArrayList<>();
    private final OnCategoryEditListener editListener;
    private final OnCategoryDeleteListener deleteListener;

    public interface OnCategoryEditListener {
        void onEdit(Category category);
    }

    public interface OnCategoryDeleteListener {
        void onDelete(Category category);
    }

    public AdminCategoryAdapter(OnCategoryEditListener editListener, OnCategoryDeleteListener deleteListener) {
        this.editListener = editListener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView categoryName;
        private final TextView categoryDescription;
        private final ImageButton btnEdit;
        private final ImageButton btnDelete;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryName);
            categoryDescription = itemView.findViewById(R.id.categoryDescription);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        public void bind(Category category) {
            categoryName.setText(category.getName());
            categoryDescription.setText(category.getDescription());

            btnEdit.setOnClickListener(v -> {
                if (editListener != null) {
                    editListener.onEdit(category);
                }
            });

            btnDelete.setOnClickListener(v -> {
                if (deleteListener != null) {
                    deleteListener.onDelete(category);
                }
            });
        }
    }
} 
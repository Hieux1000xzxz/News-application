package com.example.newspaper.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newspaper.R;
import com.example.newspaper.models.Category;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class AdminCategoryAdapter extends RecyclerView.Adapter<AdminCategoryAdapter.CategoryViewHolder> implements Filterable {
    private List<Category> categories = new ArrayList<>();
    private List<Category> categoriesFull = new ArrayList<>();
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
        this.categoriesFull = new ArrayList<>(categories);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return categoryFilter;
    }

    private Filter categoryFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Category> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(categoriesFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Category category : categoriesFull) {
                    if (category.getName().toLowerCase().contains(filterPattern) ||
                            (category.getId() != null && category.getId().toString().contains(filterPattern)) ||
                            (category.getDescription() != null && category.getDescription().toLowerCase().contains(filterPattern))) {
                        filteredList.add(category);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            categories.clear();
            categories.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView categoryId;
        private final TextView categoryName;
        private final MaterialButton btnEdit;
        private final MaterialButton btnDelete;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryId = itemView.findViewById(R.id.categoryId);
            categoryName = itemView.findViewById(R.id.categoryName);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        public void bind(Category category) {
            categoryId.setText(category.getId() != null ? category.getId().toString() : "");
            categoryName.setText(category.getName());

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
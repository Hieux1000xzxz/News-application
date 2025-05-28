package com.example.newspaper.admin.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newspaper.R;
import com.example.newspaper.admin.view_holder.CategoryManagerViewHolder;
import com.example.newspaper.common.models.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryManagerAdapter extends RecyclerView.Adapter<CategoryManagerViewHolder> implements Filterable {

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

    public CategoryManagerAdapter(OnCategoryEditListener editListener, OnCategoryDeleteListener deleteListener) {
        this.editListener = editListener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public CategoryManagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_category, parent, false);
        return new CategoryManagerViewHolder(view, editListener, deleteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryManagerViewHolder holder, int position) {
        holder.bind(categories.get(position));
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

    private final Filter categoryFilter = new Filter() {
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
            categories.addAll((List<Category>) results.values);
            notifyDataSetChanged();
        }
    };
}

package com.example.newspaper.admin.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newspaper.R;
import com.example.newspaper.admin.view_holder.ArticleManagerViewHolder;
import com.example.newspaper.common.models.Article;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ArticleManagerAdapter extends RecyclerView.Adapter<ArticleManagerViewHolder> {
    private List<Article> articles = new ArrayList<>(); // Khởi tạo rỗng
    private final Consumer<Article> onEdit;
    private final Consumer<Article> onDelete;

    public ArticleManagerAdapter(Consumer<Article> onEdit, Consumer<Article> onDelete) {
        this.onEdit = onEdit;
        this.onDelete = onDelete;
    }

    public void submitList(List<Article> newList) {
        this.articles = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ArticleManagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_article, parent, false);
        return new ArticleManagerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleManagerViewHolder holder, int position) {
        Article article = articles.get(position);
        holder.tvTitle.setText(article.getTitle());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                .withZone(ZoneId.systemDefault());

        if (article.getPublishedAt() != null) {
            String formatted = formatter.format(article.getPublishedAt());
            holder.tvStatus.setText("Đã đăng: " + formatted);
        } else {
            holder.tvStatus.setText("Chưa đăng");
        }

        Glide.with(holder.itemView.getContext())
                .load(article.getThumbnailUrl())
                .into(holder.imgThumbnail);

        holder.btnEdit.setOnClickListener(v -> onEdit.accept(article));
        holder.btnDelete.setOnClickListener(v -> onDelete.accept(article));
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }
}

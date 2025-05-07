package com.example.newsapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.newsapplication.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapplication.Model.NewsItem;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<NewsItem> newsList;

    public NewsAdapter(List<NewsItem> newsList) {
        this.newsList = newsList;
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView timeText;
        TextView categoryText;
        TextView titleText;
        TextView descriptionText;
        View categoryDot;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            timeText = itemView.findViewById(R.id.timeText);
            categoryText = itemView.findViewById(R.id.categoryText);
            titleText = itemView.findViewById(R.id.titleText);
            descriptionText = itemView.findViewById(R.id.descriptionText);
            categoryDot = itemView.findViewById(R.id.categoryDot);
        }
    }
    // Thêm phương thức để cập nhật danh sách tin tức
    public void updateNewsList(List<NewsItem> newsList) {
        this.newsList = newsList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news, parent, false);

        return new NewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsItem currentItem = newsList.get(position);

        holder.timeText.setText(currentItem.getTime());
        holder.categoryText.setText(currentItem.getCategory());
        holder.titleText.setText(currentItem.getTitle());
        holder.descriptionText.setText(currentItem.getDescription());

        // Thiết lập màu cho chấm tròn dựa vào chuyên mục
        switch (currentItem.getCategory()) {
            case "Pháp luật":
                holder.categoryDot.setBackgroundResource(R.drawable.circle_green);
                break;
            case "Xã hội":
                holder.categoryDot.setBackgroundResource(R.drawable.circle_green);
                break;
            case "Thế giới":
                holder.categoryDot.setBackgroundResource(R.drawable.circle_green);
                break;
            default:
                holder.categoryDot.setBackgroundResource(R.drawable.circle_green);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }
}
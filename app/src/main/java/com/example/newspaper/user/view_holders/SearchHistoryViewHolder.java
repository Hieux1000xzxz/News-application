package com.example.newspaper.user.view_holders;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newspaper.R;
import com.example.newspaper.user.activities.SearchArticleActivity;
import com.example.newspaper.common.database.repositories.SearchHistoryRepository;
import com.example.newspaper.common.models.SearchHistory;

public class SearchHistoryViewHolder extends BaseViewHolder<SearchHistory>{
    TextView historyTopic;
    ImageView deleteIcon;
    SearchHistory searchHistory;
    SearchHistoryRepository repository;

    public SearchHistoryViewHolder(@NonNull View itemView) {
        super(itemView);
        historyTopic = findViewById(R.id.history_topic);
        deleteIcon = findViewById(R.id.delete);
        repository = new SearchHistoryRepository(itemView.getContext());

        itemView.setOnClickListener(v -> {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                String keyword = historyTopic.getText().toString().trim();
                Intent intent = new Intent(itemView.getContext(), SearchArticleActivity.class);
                intent.putExtra("keyword", keyword);
                itemView.getContext().startActivity(intent);
            }
        });

        deleteIcon.setOnClickListener(v -> {
            repository.delete(searchHistory);
        });
    }

    @Override
    public void onBindViewHolder(SearchHistory item) {
        historyTopic.setText(item.getKeyword());
        searchHistory = item;
    }
}

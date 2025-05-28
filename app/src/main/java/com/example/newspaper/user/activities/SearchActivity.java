package com.example.newspaper.user.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newspaper.R;
import com.example.newspaper.common.database.repositories.SearchHistoryRepository;
import com.example.newspaper.common.models.SearchHistory;
import com.example.newspaper.user.adapters.SearchHistoryAdapter;
import com.example.newspaper.user.view_models.SearchViewModel;
import com.google.android.material.button.MaterialButton;

import java.time.LocalDate;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    SearchViewModel viewModel;
    SearchHistoryRepository repository;
    private List<String> trends;
    private ListView listTrends;
    private RecyclerView listHistories;
    private EditText textKeyword;
    private Button deleteAllBtn;
    private Dialog dialog;
    private MaterialButton confirm, cancel;
    TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);

        ImageView searchBox = findViewById(R.id.back_btn);
        searchBox.setOnClickListener(v -> {
            finish();
        });

        setDialog();
        mapping();

        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        viewModel.histories.observe(this, histories -> {
            SearchHistoryAdapter historyAdapter = new SearchHistoryAdapter(histories);
            listHistories.setAdapter(historyAdapter);
        });

        viewModel.trends.observe(this, trends -> {
            ArrayAdapter<String> trendAdapter = new ArrayAdapter<>(this, R.layout.item_trend_search_view, R.id.trend_topic, trends);
            listTrends.setAdapter(trendAdapter);
        });

        listTrends.setOnItemClickListener((parent, view, position, id) -> {
            String keyword = viewModel.trends.getValue().get(position);
            Intent intent = new Intent(SearchActivity.this, SearchArticleActivity.class);
            intent.putExtra("keyword", keyword);
            startActivity(intent);
        });

        SharedPreferences prefs = getApplication().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        int userId = (int) prefs.getLong("userId", -1);
        textKeyword.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                String keyword = textKeyword.getText().toString().trim();
                Intent intent = new Intent(SearchActivity.this, SearchArticleActivity.class);
                intent.putExtra("keyword", keyword);
                if(userId != -1){
                    repository.insert(SearchHistory.builder()
                            .keyword(keyword)
                            .userId(userId)
                            .searchAt(LocalDate.now())
                            .build());
                }
                startActivity(intent);
                return true;
            }
            return false;
        });

        if(viewModel.histories == null){
            deleteAllBtn.setVisibility(ListView.INVISIBLE);
        }else{
            deleteAllBtn.setVisibility(ListView.VISIBLE);
        }

        deleteAllBtn.setOnClickListener(v -> {
            dialog.show();
        });
    }

    public void mapping(){
        listTrends = findViewById(R.id.list_trend);
        listHistories = findViewById(R.id.list_history);
        listHistories.setLayoutManager(new LinearLayoutManager(this));
        textKeyword = findViewById(R.id.textKeyword);
        deleteAllBtn = findViewById(R.id.delete_all);

        repository = new SearchHistoryRepository(this);
    }

    public void setDialog(){
        dialog = new Dialog(SearchActivity.this);
        dialog.setContentView(R.layout.dialog_view);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_custom_dialog);
        dialog.setCancelable(false);

        confirm = dialog.findViewById(R.id.confirm);
        cancel = dialog.findViewById(R.id.cancel);
        message = dialog.findViewById(R.id.message);
        message.setText("Bạn có chắc muốn xóa hêts lịch sử tìm kiếm không");

        confirm.setOnClickListener(v1 -> {
            SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
            int id = (int) prefs.getLong("userId", -1);
            repository.deleteByUserId(id);
            dialog.dismiss();
        });
        cancel.setOnClickListener(v1 -> dialog.dismiss());
    }
}
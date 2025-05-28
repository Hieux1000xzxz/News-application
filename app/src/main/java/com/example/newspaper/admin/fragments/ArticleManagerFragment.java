package com.example.newspaper.admin.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newspaper.admin.activities.AddOrEditArticle;
import com.example.newspaper.admin.adapter.ArticleManagerAdapter;
import com.example.newspaper.admin.view_models.ArticleManagerViewModel;
import com.example.newspaper.common.models.Article;
import com.example.newspaper.common.models.Category;
import com.example.newspaper.databinding.FragmentManagerArticleBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ArticleManagerFragment extends Fragment {

    private ArticleManagerViewModel viewModel;
    private FragmentManagerArticleBinding binding;
    private RecyclerView recyclerView;
    private FloatingActionButton fabAdd;
    private ArticleManagerAdapter adapter;
    private List<Article> articleList;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ArticleManagerViewModel articleManagerViewModel = new ViewModelProvider(this).get(ArticleManagerViewModel.class);

        binding = FragmentManagerArticleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        viewModel = new ViewModelProvider(this).get(ArticleManagerViewModel.class);

        recyclerView = binding.recyclerViewArticles;
        fabAdd = binding.fabAddArticle;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        fabAdd = binding.fabAddArticle;

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), AddOrEditArticle.class);
            startActivity(intent);
        });

        return root;
    }

    private void loadArticles() {
        viewModel.getAllArticle().observe(this, articles -> {
            adapter = new ArticleManagerAdapter(
                    article -> {
                        Intent intent = new Intent(requireContext(), AddOrEditArticle.class);
                        intent.putExtra("articleId", article.getId());
                        startActivity(intent);
                    },
                    this::showDeleteConfirmationDialog
            );

            adapter.submitList(articles);
            recyclerView.setAdapter(adapter);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadArticles();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void showDeleteConfirmationDialog(Article article) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xóa chuyên mục")
                .setMessage("Bạn có chắc chắn muốn xóa bài viết này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    viewModel.delete(article);
                    Toast.makeText(requireContext(), "Đã xóa bài viết!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
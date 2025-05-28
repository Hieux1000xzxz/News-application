package com.example.newspaper.user.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newspaper.user.activities.NewsPointActivity;
import com.example.newspaper.R;
import com.example.newspaper.common.models.Category;
import com.example.newspaper.user.adapters.ArticleRecycleViewAdapter;
import com.example.newspaper.user.adapters.CategoryRecycleViewAdapter;
import com.example.newspaper.user.view_items.ArticleViewItem;
import com.example.newspaper.user.view_models.ArticleViewModel;
import com.example.newspaper.user.view_models.CategoryViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class FragmentCategory extends Fragment {
    private CategoryViewModel categoryViewModel;
    private ArticleViewModel articleViewModel;
    private CategoryRecycleViewAdapter categoryAdapter;
    private RecyclerView newsRecyclerView, categoryRecyclerView;
    private TabLayout tabLayout;
    private ImageButton menuButton;
    private BottomSheetDialog categoriesDialog;
    private List<Category> categories;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categoryViewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);
        articleViewModel = new ViewModelProvider(requireActivity()).get(ArticleViewModel.class);

        // Khởi tạo RecyclerView
        newsRecyclerView = view.findViewById(R.id.newsRecyclerView);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        categories = new ArrayList<>();

        tabLayout = view.findViewById(R.id.tabLayout);

        loadArticleByCategory("Kinh doanh");
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                loadArticleByCategory(tab.getText().toString());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        // Thêm xử lý cho nút menu
        menuButton = view.findViewById(R.id.menuButton);
        if (menuButton != null) {
            menuButton.setOnClickListener(v -> showCategoriesDialog());
        }

        // Khởi tạo categories dialog
        setupCategoriesDialog();

    }

    private void setupCategoriesDialog() {
        categoriesDialog = new BottomSheetDialog(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.menu_categories, null);
        categoriesDialog.setContentView(dialogView);

        // Tìm và thiết lập sự kiện cho nút đóng
        ImageButton closeButton = dialogView.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> categoriesDialog.dismiss());

        categoryRecyclerView = categoriesDialog.findViewById(R.id.listCategories);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        categoryViewModel.getAllComment().observe(requireActivity(), categories -> {
            categories.forEach(c -> tabLayout.addTab(tabLayout.newTab().setText(c.getName())));
            this.categories.addAll(categories);
            categoryAdapter = new CategoryRecycleViewAdapter(categories);
            categoryRecyclerView.setAdapter(categoryAdapter);
        });

        setupCategoryClickListeners(dialogView);
    }

    public void setArticles(int id){
        articleViewModel.loadCategoryArticles(id);
        articleViewModel.getCategoryArticles().observe(requireActivity(), articles -> {
            List<ArticleViewItem> items = new ArrayList<>();
            articles.forEach(a -> items.add(new ArticleViewItem(a, ArticleViewItem.TypeDisplay.CATEGORY)));
            ArticleRecycleViewAdapter adapter = new ArticleRecycleViewAdapter(items);
            newsRecyclerView.setAdapter(adapter);
        });
    }

    private void setupCategoryClickListeners(View dialogView) {
        // Chuyên mục Tin mới nhất
        View latestNewsCategory = dialogView.findViewById(R.id.latestNewsCategory);
        latestNewsCategory.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), NewsPointActivity.class);
            intent.putExtra("type", "newest");
            startActivity(intent);
        });

        // Chuyên mục Điểm tin nổi bật
        View featuredNewsCategory = dialogView.findViewById(R.id.featuredNewsCategory);
        featuredNewsCategory.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), NewsPointActivity.class);
            intent.putExtra("type", "new-point");
            startActivity(intent);
        });
    }

    private void showCategoriesDialog() {
        if (categoriesDialog != null) {
            categoriesDialog.show();
        }
    }

    public void navigate(View parentView, int viewId, Class<?> targetActivity) {
        View button = parentView.findViewById(viewId);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), targetActivity);
            startActivity(intent);
        });
    }

    public int getCategoryIdByName(String name){
        for (Category c : categories){
            if (c.getName().equals(name)) return c.getId();
        }
        return 1;
    }

    public void loadArticleByCategory(String name){
        articleViewModel.loadCategoryArticles(getCategoryIdByName(name));
        articleViewModel.getCategoryArticles().observe(requireActivity(), articles -> {
            List<ArticleViewItem> items = new ArrayList<>();
            articles.forEach(a -> items.add(new ArticleViewItem(a, ArticleViewItem.TypeDisplay.CATEGORY)));
            ArticleRecycleViewAdapter adapter = new ArticleRecycleViewAdapter(items);
            newsRecyclerView.setAdapter(adapter);
        });
    }
}

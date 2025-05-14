package com.example.newspaper.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.example.newspaper.NewsPointActivity;
import com.example.newspaper.R;
import com.example.newspaper.models.Article;
import com.example.newspaper.models.Category;
import com.example.newspaper.pojo.ArticleWithCategory;
import com.example.newspaper.ui.adapters.ArticleRecycleViewAdapter;
import com.example.newspaper.ui.adapters.CategoryAdapter;
import com.example.newspaper.ui.adapters.view_items.ArticleViewItem;
import com.example.newspaper.ui.adapters.view_models.ArticleWithCategoryViewModel;
import com.example.newspaper.ui.adapters.view_models.CategoryViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class FragmentCategory extends Fragment {
    private static final String TAG = "FragmentCategory";
    private RecyclerView newsRecyclerView;
    private TabLayout tabLayout;
    private ImageButton menuButton;
    private BottomSheetDialog categoriesDialog;
    private ArticleRecycleViewAdapter adapter;
    private ArticleWithCategoryViewModel viewModel;
    private CategoryViewModel categoryViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Khởi tạo ViewModel
        viewModel = new ViewModelProvider(this).get(ArticleWithCategoryViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        // Khởi tạo RecyclerView
        newsRecyclerView = view.findViewById(R.id.newsRecyclerView);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        // Tạo adapter với danh sách rỗng ban đầu
        adapter = new ArticleRecycleViewAdapter(new ArrayList<>());
        newsRecyclerView.setAdapter(adapter);

        // Quan sát dữ liệu từ ViewModel
        observeArticleData();
        observeCategoryData();

        // Thiết lập TabLayout
        setupTabLayout(view);

        // Thêm xử lý cho nút menu
        menuButton = view.findViewById(R.id.menuButton);
        if (menuButton != null) {
            menuButton.setOnClickListener(v -> showCategoriesDialog());
        }

        // Khởi tạo categories dialog
        setupCategoriesDialog();
    }

    private void observeCategoryData() {
        categoryViewModel.getAllCategories().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null) {
                Log.d(TAG, "Categories updated: " + categories.size());
                updateTabLayout(categories);
            }
        });
    }

    private void updateTabLayout(List<Category> categories) {
        if (tabLayout == null) return;

        // Xóa tất cả tab hiện tại
        tabLayout.removeAllTabs();

        // Thêm tab "Tất cả" đầu tiên
        tabLayout.addTab(tabLayout.newTab().setText("Tất cả"));

        // Thêm các tab từ danh mục trong database
        for (Category category : categories) {
            tabLayout.addTab(tabLayout.newTab().setText(category.getName()));
        }
    }

    private void setupTabLayout(View view) {
        tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    // Tab "Tất cả"
                    loadNewsByCategory("all");
                } else {
                    // Lấy danh mục từ database
                    List<Category> categories = categoryViewModel.getAllCategories().getValue();
                    if (categories != null && tab.getPosition() - 1 < categories.size()) {
                        Category selectedCategory = categories.get(tab.getPosition() - 1);
                        loadNewsByCategory(selectedCategory.getName());
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void observeArticleData() {
        viewModel.getArticlesByCategory().observe(getViewLifecycleOwner(), articles -> {
            if (articles != null) {
                Log.d(TAG, "Articles updated: " + articles.size());
                updateArticlesList(articles);
            }
        });
    }

    private void updateArticlesList(List<ArticleWithCategory> articles) {
        List<ArticleViewItem> items = new ArrayList<>();
        for (ArticleWithCategory article : articles) {
            items.add(new ArticleViewItem(article, ArticleViewItem.TypeDisplay.CATEGORY));
        }
        adapter.updateItems(items);
    }

    private void setupCategoriesDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.menu_categories, null);
        categoriesDialog = new BottomSheetDialog(requireContext());
        categoriesDialog.setContentView(dialogView);

        // Setup RecyclerView for categories
        RecyclerView categoriesRecyclerView = dialogView.findViewById(R.id.categoriesRecyclerView);
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        CategoryAdapter categoryAdapter = new CategoryAdapter(category -> {
            loadNewsByCategory(category.getName());
            categoriesDialog.dismiss();
        });
        categoriesRecyclerView.setAdapter(categoryAdapter);

        // Observe categories from ViewModel
        categoryViewModel.getAllCategories().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null) {
                categoryAdapter.setCategories(categories);
            }
        });

        // Setup close button
        ImageButton closeButton = dialogView.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> categoriesDialog.dismiss());

        // Setup other category click listeners
        setupCategoryClickListeners(dialogView);
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

        // Thiết lập sự kiện cho các chuyên mục khác
        setupMoreCategories(dialogView);
    }

    private void setupMoreCategories(View dialogView) {
        // Lấy danh sách danh mục từ database
        List<Category> categories = categoryViewModel.getAllCategories().getValue();
        if (categories == null) return;

        // Thiết lập sự kiện click cho từng danh mục
        for (Category category : categories) {
            String viewId = category.getName().toLowerCase().replace(" ", "_") + "Category";
            int resId = getResources().getIdentifier(viewId, "id", requireContext().getPackageName());
            View categoryView = dialogView.findViewById(resId);
            
            if (categoryView != null) {
                categoryView.setOnClickListener(v -> {
                    loadNewsByCategory(category.getName());
                    categoriesDialog.dismiss();
                });
            }
        }
    }

    private void showCategoriesDialog() {
        if (categoriesDialog != null) {
            categoriesDialog.show();
        }
    }

    private void loadNewsByCategory(String category) {
        Log.d(TAG, "Loading news for category: " + category);
        viewModel.setCategoryFilter(category);
    }

    public void navigate(View parentView, int viewId, Class<?> targetActivity) {
        View button = parentView.findViewById(viewId);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), targetActivity);
            startActivity(intent);
        });
    }
}
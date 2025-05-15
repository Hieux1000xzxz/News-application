package com.example.newspaper;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newspaper.models.Category;
import com.example.newspaper.ui.adapters.AdminCategoryAdapter;
import com.example.newspaper.ui.adapters.view_models.CategoryViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDate;
import java.util.List;

public class AdminCategoryActivity extends AppCompatActivity {
    private CategoryViewModel categoryViewModel;
    private AdminCategoryAdapter adapter;
    private TextInputEditText searchEditText;
    private RecyclerView recyclerView;
    private LinearLayout emptyStateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        // Initialize ViewModel
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        // Find views
        recyclerView = findViewById(R.id.categoriesRecyclerView);
        emptyStateView = findViewById(R.id.emptyStateView);
        MaterialButton btnAddCategory = findViewById(R.id.btnAddCategory);
        searchEditText = findViewById(R.id.searchEditText);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        adapter = new AdminCategoryAdapter(
                category -> showEditDialog(category),
                category -> showDeleteConfirmationDialog(category)
        );
        recyclerView.setAdapter(adapter);

        // Observe categories
        categoryViewModel.getAllCategoriesById().observe(this, categories -> {
            adapter.setCategories(categories);
            updateEmptyState(categories);
        });

        // Setup Add button click
        btnAddCategory.setOnClickListener(v -> showAddDialog());

        // Setup search functionality
        setupSearchFunctionality();
    }

    private void setupSearchFunctionality() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                adapter.getFilter().filter(s.toString());
            }
        });
    }

    private void updateEmptyState(List<Category> categories) {
        if (categories == null || categories.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyStateView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyStateView.setVisibility(View.GONE);
        }
    }

    private void showAddDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_category, null);
        TextInputEditText editTextName = dialogView.findViewById(R.id.editTextCategoryName);
        TextInputEditText editTextDescription = dialogView.findViewById(R.id.editTextCategoryDescription);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Thêm chuyên mục mới")
                .setView(dialogView)
                .setPositiveButton("Thêm", null) // Null listener here, we'll override it later
                .setNegativeButton("Hủy", null)
                .create();

        dialog.setOnShowListener(dialogInterface -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view -> {
                String name = editTextName.getText().toString().trim();
                String description = editTextDescription.getText().toString().trim();

                if (name.isEmpty()) {
                    editTextName.setError("Bạn chưa nhập tên chuyên mục.Không thể thêm chuyên mục.");
                    return;
                }

                Category category = new Category(null, name, description, LocalDate.now());
                categoryViewModel.insert(category);
                Toast.makeText(this, "Đã thêm chuyên mục mới", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });
        });

        dialog.show();
    }

    private void showEditDialog(Category category) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_category, null);
        TextInputEditText editTextName = dialogView.findViewById(R.id.editTextCategoryName);
        TextInputEditText editTextDescription = dialogView.findViewById(R.id.editTextCategoryDescription);

        editTextName.setText(category.getName());
        editTextDescription.setText(category.getDescription());

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Sửa chuyên mục")
                .setView(dialogView)
                .setPositiveButton("Lưu", null) // Null listener here, we'll override it later
                .setNegativeButton("Hủy", null)
                .create();

        dialog.setOnShowListener(dialogInterface -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view -> {
                String name = editTextName.getText().toString().trim();
                String description = editTextDescription.getText().toString().trim();

                if (name.isEmpty()) {
                    editTextName.setError("Vui lòng nhập tên chuyên mục");
                    return;
                }

                category.setName(name);
                category.setDescription(description);
                categoryViewModel.update(category);
                Toast.makeText(this, "Đã cập nhật chuyên mục", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });
        });

        dialog.show();
    }

    private void showDeleteConfirmationDialog(Category category) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa chuyên mục")
                .setMessage("Bạn có chắc chắn muốn xóa chuyên mục này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    categoryViewModel.delete(category);
                    Toast.makeText(this, "Đã xóa chuyên mục", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
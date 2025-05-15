package com.example.newspaper;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newspaper.models.Category;
import com.example.newspaper.ui.adapters.AdminCategoryAdapter;
import com.example.newspaper.ui.adapters.view_models.CategoryViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDate;

public class AdminCategoryActivity extends AppCompatActivity {
    private CategoryViewModel categoryViewModel;
    private AdminCategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize ViewModel
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        // Setup RecyclerView
        RecyclerView recyclerView = findViewById(R.id.categoriesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdminCategoryAdapter(
                category -> showEditDialog(category),
                category -> showDeleteConfirmationDialog(category)
        );
        recyclerView.setAdapter(adapter);

        // Observe categories
        categoryViewModel.getAllCategories().observe(this, categories -> {
            adapter.setCategories(categories);
        });

        // Setup FAB
        FloatingActionButton fab = findViewById(R.id.fabAddCategory);
        fab.setOnClickListener(v -> showAddDialog());
    }

    private void showAddDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_category, null);
        TextInputEditText editTextName = dialogView.findViewById(R.id.editTextCategoryName);
        TextInputEditText editTextDescription = dialogView.findViewById(R.id.editTextCategoryDescription);

        new AlertDialog.Builder(this)
                .setTitle("Thêm chuyên mục mới")
                .setView(dialogView)
                .setPositiveButton("Thêm", (dialog, which) -> {
                    String name = editTextName.getText().toString().trim();
                    String description = editTextDescription.getText().toString().trim();

                    if (name.isEmpty()) {
                        Toast.makeText(this, "Vui lòng nhập tên chuyên mục", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Category category = new Category(null, name, description, LocalDate.now());
                    categoryViewModel.insert(category);
                    Toast.makeText(this, "Đã thêm chuyên mục mới", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void showEditDialog(Category category) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_category, null);
        TextInputEditText editTextName = dialogView.findViewById(R.id.editTextCategoryName);
        TextInputEditText editTextDescription = dialogView.findViewById(R.id.editTextCategoryDescription);

        editTextName.setText(category.getName());
        editTextDescription.setText(category.getDescription());

        new AlertDialog.Builder(this)
                .setTitle("Sửa chuyên mục")
                .setView(dialogView)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String name = editTextName.getText().toString().trim();
                    String description = editTextDescription.getText().toString().trim();

                    if (name.isEmpty()) {
                        Toast.makeText(this, "Vui lòng nhập tên chuyên mục", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    category.setName(name);
                    category.setDescription(description);
                    categoryViewModel.update(category);
                    Toast.makeText(this, "Đã cập nhật chuyên mục", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
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
package com.example.newspaper.admin.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newspaper.R;
import com.example.newspaper.admin.adapter.CategoryManagerAdapter;
import com.example.newspaper.admin.view_models.CategoryManagerViewModel;
import com.example.newspaper.common.models.Category;
import com.example.newspaper.databinding.FragmentManagerCategoryBinding;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class CategoryManagerFragment extends Fragment {
    private FragmentManagerCategoryBinding binding;
    private CategoryManagerViewModel categoryViewModel;
    private CategoryManagerAdapter adapter;
    private TextInputEditText searchEditText;
    private RecyclerView recyclerView;
    private LinearLayout emptyStateView;
    private FloatingActionButton btnAddCategory;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentManagerCategoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize ViewModel
        categoryViewModel = new ViewModelProvider(this).get(CategoryManagerViewModel.class);

        // Find views
        recyclerView = binding.categoriesRecyclerView;
        emptyStateView = binding.emptyStateView;
        btnAddCategory = binding.btnAddCategory;
        searchEditText = binding.searchEditText;

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        adapter = new CategoryManagerAdapter(
                this::showEditDialog,
                this::showDeleteConfirmationDialog
        );

        // Observe categories
        categoryViewModel.getAllCategoriesById().observe(requireActivity(), categories -> {
            adapter.setCategories(categories);
            updateEmptyState(categories);
            recyclerView.setAdapter(adapter);
        });

        // Setup Add button click
        btnAddCategory.setOnClickListener(v -> showAddDialog());

        // Setup search functionality
        setupSearchFunctionality();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle("Thêm chuyên mục mới")
                .setView(dialogView)
                .setPositiveButton("Thêm", null)
                .setNegativeButton("Hủy", null)
                .create();

        dialog.setOnShowListener(dialogInterface -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view -> {
            String name = Objects.requireNonNull(editTextName.getText()).toString().trim();
            String description = Objects.requireNonNull(editTextDescription.getText()).toString().trim();

            if (name.isEmpty()) {
                // Hiển thị thông báo bằng Toast thay vì setError
                Toast.makeText(requireContext(), "Bạn chưa nhập tên chuyên mục. Không thể thêm chuyên mục.", Toast.LENGTH_SHORT).show();
                // Không đóng dialog để người dùng có thể tiếp tục nhập
                return;
            }

            Category category = new Category(null, name, description, LocalDate.now());
            categoryViewModel.insert(category);
            Toast.makeText(requireContext(), "Đã thêm chuyên mục mới", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }));

        dialog.show();
    }

    private void showEditDialog(Category category) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_category, null);
        TextInputEditText editTextName = dialogView.findViewById(R.id.editTextCategoryName);
        TextInputEditText editTextDescription = dialogView.findViewById(R.id.editTextCategoryDescription);

        editTextName.setText(category.getName());
        editTextDescription.setText(category.getDescription());

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle("Sửa chuyên mục")
                .setView(dialogView)
                .setPositiveButton("Lưu", null)
                .setNegativeButton("Hủy", null)
                .create();

        dialog.setOnShowListener(dialogInterface -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view -> {
            String name = editTextName.getText().toString().trim();
            String description = editTextDescription.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(requireContext(), "Bạn chưa nhập tên chuyên mục. Không thể cập nhật chuyên mục.", Toast.LENGTH_SHORT).show();
                return;
            }

            category.setName(name);
            category.setDescription(description);
            categoryViewModel.update(category);
            Toast.makeText(requireContext(), "Đã cập nhật chuyên mục", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }));

        dialog.show();
    }

    private void showDeleteConfirmationDialog(Category category) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xóa chuyên mục")
                .setMessage("Bạn có chắc chắn muốn xóa chuyên mục này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    categoryViewModel.delete(category);
                    Toast.makeText(requireContext(), "Đã xóa chuyên mục", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            requireActivity().getSupportFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
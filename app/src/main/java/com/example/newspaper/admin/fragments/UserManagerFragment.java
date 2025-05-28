package com.example.newspaper.admin.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.newspaper.R;
import com.example.newspaper.admin.adapter.UserManagerAdapter;
import com.example.newspaper.admin.view_models.UserManagerViewModel;
import com.example.newspaper.common.database.repositories.UserRepository;
import com.example.newspaper.common.models.User;
import com.example.newspaper.databinding.FragmentManagerUserBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UserManagerFragment extends Fragment {
    private ListView listViewUsers;
    private Uri selectedImageUri;
    private UserRepository userRepository;
    private UserManagerAdapter userAdapter;
    private TextInputEditText searchEditText;
    private List<User> filteredUserList;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private List<User> userList;
    private ActivityResultLauncher<Intent> addUserLauncher;
    private ImageView activeAvatarImageView;

    private FragmentManagerUserBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        UserManagerViewModel userManagerViewModel =
                new ViewModelProvider(this).get(UserManagerViewModel.class);

        binding = FragmentManagerUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        filteredUserList = new ArrayList<>();
        listViewUsers = binding.listViewUsers;
        searchEditText = binding.searchEditText;
        userRepository = new UserRepository(requireActivity().getApplication());
        userList = new ArrayList<>();
        userAdapter = new UserManagerAdapter(requireContext(), filteredUserList);


        listViewUsers.setAdapter(userAdapter);
        FloatingActionButton fabAddUser = binding.fabAddUser;
        fabAddUser.setOnClickListener(v -> showAddUserDialog());

        addUserLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        loadUsers();
                        Toast.makeText(requireContext(), "Danh sách người dùng đã được cập nhật", Toast.LENGTH_SHORT).show();
                    }
                });

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null && activeAvatarImageView != null) {
                            activeAvatarImageView.setImageURI(selectedImageUri);
                            Log.d("ManageUsersActivity", "Image selected: " + selectedImageUri.toString());
                        }
                    }
                });

        loadUsers();

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterUsers(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        //click vào người dùng
        listViewUsers.setOnItemClickListener((parent, view, position, id) -> {
            User user = userList.get(position);
            showEditUserDialog(user);
        });

        // long click để xóa
        listViewUsers.setOnItemLongClickListener((parent, view, position, id) -> {
            User user = filteredUserList.get(position);
            showDeleteConfirmationDialog(user);
            return true;
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void showEditUserDialog(User user) {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_user, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(dialogView);

        ImageView imgAvatar = dialogView.findViewById(R.id.imgAvatar);
        ImageButton btnAddPhoto = dialogView.findViewById(R.id.btnAddPhoto);
        EditText edName = dialogView.findViewById(R.id.edName);
        EditText etEmail = dialogView.findViewById(R.id.etEmail);
        EditText etBirthday = dialogView.findViewById(R.id.etDate);
        EditText etGender = dialogView.findViewById(R.id.etGender);
        EditText etPhone = dialogView.findViewById(R.id.etPhone);
        EditText etAddress = dialogView.findViewById(R.id.etAddress);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnUpdate = dialogView.findViewById(R.id.btnUpdate);

        activeAvatarImageView = imgAvatar;
        if (user.getAvatarUrl() != null) {
            try {
                imgAvatar.setImageURI(Uri.parse(user.getAvatarUrl()));
                Log.d("ManageUsersActivity", "Loaded existing avatar: " + user.getAvatarUrl());
            } catch (Exception e) {
                Log.e("ManageUsersActivity", "Error loading avatar: " + e.getMessage());
            }
        }

        // Populate user info
        edName.setText(user.getUsername() != null ? user.getUsername() : "");
        etEmail.setText(user.getEmail() != null ? user.getEmail() : "");
        if (user.getDob() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            etBirthday.setText(sdf.format(user.getDob()));
        }
        etGender.setText(user.getMale() != null ? (user.getMale() ? "Nam" : "Nữ") : "Khác");
        etPhone.setText(user.getPhone() != null ? user.getPhone() : "");
        etAddress.setText(user.getCity() != null ? user.getCity() : "");

        AlertDialog dialog = builder.create();

        //  photo selection
        btnAddPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });

//        imagePickerLauncher.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
//            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
//                selectedImageUri = result.getData().getData();
//                if (selectedImageUri != null) {
//                    imgAvatar.setImageURI(selectedImageUri);
//                }
//            }
//        });

        //  date picker
        etBirthday.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireContext(),
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        etBirthday.setText(selectedDate);
                    },
                    year, month, day);
            datePickerDialog.show();
        });

        //  Cancel button
        btnCancel.setOnClickListener(v -> {
            activeAvatarImageView = null;
            dialog.dismiss();
        });

        //  Update button
        btnUpdate.setOnClickListener(v -> {
            if (!validateEditInput(edName, etEmail, etPhone)) {
                return;
            }

            User updatedUser = new User();
            updatedUser.setId((int)user.getId());
            updatedUser.setUsername(edName.getText().toString().trim());
            updatedUser.setEmail(etEmail.getText().toString().trim());
            updatedUser.setPasswordHash(user.getPasswordHash());
            updatedUser.setRole(user.getRole());
            updatedUser.setAvatarUrl(selectedImageUri != null ? selectedImageUri.toString() : user.getAvatarUrl());

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String birthdayStr = etBirthday.getText().toString().trim();
                if (!birthdayStr.isEmpty()) {
                    Date dob = sdf.parse(birthdayStr);
                    updatedUser.setDob(dob);
                } else {
                    updatedUser.setDob(null);
                }
            } catch (Exception e) {
                Toast.makeText(requireContext(), "Ngày sinh không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            String genderStr = etGender.getText().toString().trim();
            if (genderStr.equalsIgnoreCase("Nam")) {
                updatedUser.setMale(true);
            } else if (genderStr.equalsIgnoreCase("Nữ")) {
                updatedUser.setMale(false);
            } else {
                updatedUser.setMale(null);
            }

            updatedUser.setPhone(etPhone.getText().toString().trim());
            updatedUser.setCity(etAddress.getText().toString().trim());

            userRepository.updateUser(updatedUser, new UserRepository.UpdateCallback() {
                @Override
                public void onSuccess() {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(requireContext(), "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        activeAvatarImageView = null;
                        loadUsers();
                    });
                }

                @Override
                public void onError(String errorMessage) {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(requireContext(), "Lỗi: " + errorMessage, Toast.LENGTH_SHORT).show();
                    });
                }
            });
        });

        dialog.show();
    }
    private boolean validateEditInput(EditText edName, EditText etEmail, EditText etPhone) {
        if (edName.getText().toString().trim().isEmpty()) {
            edName.setError("Vui lòng nhập họ tên");
            return false;
        }
        if (etEmail.getText().toString().trim().isEmpty()) {
            etEmail.setError("Vui lòng nhập email");
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString().trim()).matches()) {
            etEmail.setError("Email không hợp lệ");
            return false;
        }
        if (etPhone.getText().toString().trim().length() < 10) {
            etPhone.setError("Số điện thoại không hợp lệ");
            return false;
        }
        return true;
    }
    //    private void setupListViewListeners() {
//        listViewUsers.setOnItemClickListener((parent, view, position, id) -> {
//            User user = userList.get(position);
//            showEditUserDialog(user);
//        });
//
//        listViewUsers.setOnItemLongClickListener((parent, view, position, id) -> {
//            User user = userList.get(position);
//            showDeleteConfirmationDialog(user, position);
//            return true;
//        });
//    }
    private void showAddUserDialog() {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_user, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(dialogView);

        EditText etUsername = dialogView.findViewById(R.id.etUsername);
        EditText etEmail = dialogView.findViewById(R.id.etEmail);
        EditText etPassword = dialogView.findViewById(R.id.etPassword);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnSave = dialogView.findViewById(R.id.btnSave);

        AlertDialog dialog = builder.create();

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        // Save button
        btnSave.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            User newUser = new User();
            newUser.setUsername(username);
            newUser.setEmail(email);
            newUser.setPasswordHash(password);
            newUser.setRole("user");

            userRepository.registerUser(newUser, new UserRepository.RegistrationCallback() {
                @Override
                public void onSuccess() {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Thêm người dùng thành công", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        loadUsers();
                    });
                }

                @Override
                public void onError(String errorMessage) {
                    requireActivity().runOnUiThread(() -> {
                        if (errorMessage.equals("Email đã được đăng ký")) {
                            etEmail.setError(errorMessage);
                            etEmail.requestFocus();
                        } else if (errorMessage.contains("tên người dùng")) {
                            etUsername.setError(errorMessage);
                            etUsername.requestFocus();
                        } else if (errorMessage.contains("email hợp lệ")) {
                            etEmail.setError(errorMessage);
                            etEmail.requestFocus();
                        } else if (errorMessage.contains("Mật khẩu")) {
                            etPassword.setError(errorMessage);
                            etPassword.requestFocus();
                        } else {
                            Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        });

        dialog.show();
    }
    private boolean validateInput(String username, String email, String password,
                                  EditText etUsername, EditText etEmail, EditText etPassword) {
        if (username.isEmpty()) {
            etUsername.setError("Vui lòng nhập tên người dùng");
            return false;
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Vui lòng nhập email hợp lệ");
            return false;
        }

        if (password.isEmpty() || password.length() < 6) {
            etPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
            return false;
        }
        return true;
    }
    private void filterUsers(String query) {
        filteredUserList.clear();
        if (query.isEmpty()) {
            filteredUserList.addAll(userList);
        } else {
            String lowerCaseQuery = query.toLowerCase(Locale.getDefault());
            for (User user : userList) {
                if (user.getUsername() != null && user.getUsername().toLowerCase(Locale.getDefault()).contains(lowerCaseQuery)) {
                    filteredUserList.add(user);
                }
            }
        }
        userAdapter.notifyDataSetChanged();
    }


    private void loadUsers() {
        userRepository.getAllUsers().observe(requireActivity(), users -> {
            if (users != null) {
                userList.clear();
                userList.addAll(users);
                filterUsers(searchEditText.getText().toString());
            }
        });
    }


    private void showDeleteConfirmationDialog(User user) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xóa người dùng")
                .setMessage("Bạn có chắc chắn muốn xóa " + user.getUsername() + "?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    userRepository.deleteUser(user, new UserRepository.DeleteCallback() {
                        @Override
                        public void onSuccess() {
                            requireActivity().runOnUiThread(() -> {
                                userList.remove(user);
                                filterUsers(searchEditText.getText().toString());
                                Toast.makeText(requireActivity(), "Đã xóa người dùng", Toast.LENGTH_SHORT).show();
                            });
                        }

                        @Override
                        public void onError(String errorMessage) {
                            requireActivity().runOnUiThread(() ->
                                    Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_SHORT).show()
                            );
                        }
                    });
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
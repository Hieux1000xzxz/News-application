package com.example.newspaper.common.database.repositories;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Patterns;

import androidx.lifecycle.LiveData;

import com.example.newspaper.common.database.DatabaseHandler;
import com.example.newspaper.common.database.dao.UserDao;
import com.example.newspaper.common.models.User;
import com.example.newspaper.common.utils.EncryptionUtil;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UserRepository {
    private final UserDao userDao;
    private final Executor executor;

    public UserRepository(Application application) {
        DatabaseHandler db = DatabaseHandler.getInstance(application);
        this.userDao = db.getUserDao();
        this.executor = Executors.newFixedThreadPool(4);
    }

    // Đăng ký người dùng mới
    public void registerUser(User user, RegistrationCallback callback) {
        // Validate đăng ký
        String validationError = validateUser(user);
        if (validationError != null) {
            callback.onError(validationError);
            return;
        }

        executor.execute(() -> {
            try {
                // Kiểm tra email đã tồn tại
                if (userDao.checkEmailExists(user.getEmail()) > 0) {
                    callback.onError("Email đã được đăng ký");
                    return;
                }

                // Thêm người dùng mới
                userDao.insert(user);
                callback.onSuccess();
            } catch (Exception e) {
                callback.onError("Lỗi hệ thống: " + e.getMessage());
            }
        });
    }

    private String validateUser(User user) {
        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
            return "Vui lòng nhập họ tên";
        }

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            return "Vui lòng nhập email";
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(user.getEmail()).matches()) {
            return "Email không hợp lệ";
        }

        if (user.getPasswordHash() == null || user.getPasswordHash().isEmpty()) {
            return "Vui lòng nhập mật khẩu";
        }

        if (user.getPasswordHash().length() < 6) {
            return "Mật khẩu phải có ít nhất 6 ký tự";
        }

        return null;
    }

    // Kiểm tra email tồn tại
    public LiveData<Boolean> checkEmailExistsLive(String email) {
        return userDao.checkEmailExistsLive(email);
    }

    // Kiểm tra email tồn tại
    public boolean checkEmailExists(String email) {
        if (email == null || email.trim().isEmpty()) {
            Log.w("UserRepository", "Invalid email: " + email);
            return false;
        }
        try {
            boolean exists = userDao.checkEmailExists(email) > 0;
            Log.d("UserRepository", "Checking email: " + email + ", exists: " + exists);
            return exists;
        } catch (Exception e) {
            Log.e("UserRepository", "Check email error: " + e.getMessage());
            return false;
        }
    }

    // Đăng nhập
    public void login(String email, String password, LoginCallback callback) {
        // Validate đăng nhập
        if (email == null || email.trim().isEmpty()) {
            callback.onError("Vui lòng nhập email");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            callback.onError("Email không hợp lệ");
            return;
        }

        if (password == null || password.isEmpty()) {
            callback.onError("Vui lòng nhập mật khẩu");
            return;
        }

        executor.execute(() -> {
            try {
                User user = userDao.getUserByEmail(email);
                if (user == null) {
                    callback.onError("Email không tồn tại");
                    return;
                }

                // BCrypt mật khẩu hash
                if (!EncryptionUtil.checkPassword(password, user.getPasswordHash())) {
                    callback.onError("Mật khẩu không chính xác");
                    return;
                }

                callback.onSuccess(user);
            } catch (Exception e) {
                callback.onError("Lỗi hệ thống: " + e.getMessage());
            }
        });
    }

    // Lấy thông tin người dùng theo ID
    public LiveData<User> getUserById(int id) {
        return userDao.getUserById(id);
    }

    // Cập nhật thông tin người dùng với validate
    public void updateUser(User user, UpdateCallback callback) {
        String validationError = validateUser(user);
        if (validationError != null) {
            callback.onError(validationError);
            return;
        }

        executor.execute(() -> {
            try {
                // Kiểm tra user có tồn tại
                User existingUser = userDao.getUserByIdImmediate(user.getId());

                if (existingUser == null) {
                    callback.onError("Người dùng không tồn tại");
                    return;
                }

                // Kiểm tra email mới có bị trùng không
                if (!existingUser.getEmail().equals(user.getEmail()) &&
                        userDao.checkEmailExists(user.getEmail()) > 0) {
                    callback.onError("Email đã được sử dụng bởi tài khoản khác");
                    return;
                }

                userDao.update(user);
                Log.d("UserRepository", "Updating user ID: " + user.getId() + ", avatarUri: " + user.getAvatarUrl());
                callback.onSuccess();
            } catch (Exception e) {
                callback.onError("Lỗi hệ thống: " + e.getMessage());
            }
        });
    }
    public void updateUserProfile(long userId, String fullName, String phone,
                                  Date dob, Boolean gender, String city, String avatarUrl) {
        DatabaseHandler.databaseWriteExecutor.execute(() -> {
            User user = userDao.getUserByIdImmediate(userId);
            if (user != null) {
                user.setFullName(fullName);
                user.setPhone(phone);
                user.setDob(dob);
                user.setMale(gender);
                user.setCity(city);
                user.setAvatarUrl(avatarUrl);
                user.setUpdatedAt(new Date(System.currentTimeMillis()));

                userDao.update(user);
            }
        });
    }



    public interface RegistrationCallback {
        void onSuccess();
        void onError(String errorMessage);
    }

    public interface LoginCallback {
        void onSuccess(User user);
        void onError(String errorMessage);
    }

    public interface UpdateCallback {
        void onSuccess();
        void onError(String errorMessage);
    }

    public interface DeleteCallback {
        void onSuccess();
        void onError(String errorMessage);
    }

    public LiveData<List<User>> getAllUsers() {
        return userDao.getAllUsers();
    }

//    public LiveData<User> getUserByIdMana(int userId) {
//        return userDao.getUserById(userId);
//    }

    public void insertUser(User user, InsertCallback callback) {
        executor.execute(() -> {
            try {
                userDao.insert(user);
                new Handler(Looper.getMainLooper()).post(callback::onSuccess);
            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() ->
                        callback.onError("Lỗi khi thêm người dùng")
                );
            }
        });
    }


    public void deleteUser(User user, DeleteCallback callback) {
        executor.execute(() -> {
            try {
                userDao.delete(user);
                new Handler(Looper.getMainLooper()).post(callback::onSuccess);
            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() ->
                        callback.onError("Lỗi khi thêm người dùng")
                );
            }
        });
    }

    public interface InsertCallback {
        void onSuccess();
        void onError(String errorMessage);
    }
}

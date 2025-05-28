package com.example.newspaper.user.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.newspaper.common.database.repositories.UserRepository;
import com.example.newspaper.common.models.User;

import java.util.Date;
import java.util.List;

public class UserViewModel extends AndroidViewModel {
    private final UserRepository repository;

    private final MutableLiveData<List<Integer>> ids = new MutableLiveData<>();

    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
    }

    public void registerUser(User user, UserRepository.RegistrationCallback callback) {
        repository.registerUser(user, callback);
    }

    public void login(String email, String password, UserRepository.LoginCallback callback) {
        repository.login(email, password, callback);
    }

    public LiveData<User> getUserById(long userId) {
        return repository.getUserById((int) userId);
    }

    public void updateUserProfile(long userId, String fullName, String phone,
                                  Date dob, Boolean gender, String city, String avatarUrl) {
        repository.updateUserProfile(userId, fullName, phone, dob, gender, city, avatarUrl);
    }

}

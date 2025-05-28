package com.example.newspaper.admin.view_models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserManagerViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public UserManagerViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is userManger fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
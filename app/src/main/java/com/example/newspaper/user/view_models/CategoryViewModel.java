package com.example.newspaper.user.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.newspaper.common.database.repositories.CategoryRepository;
import com.example.newspaper.common.models.Category;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {
    private final CategoryRepository repository;
    private final MutableLiveData<List<Category>> categories = new MutableLiveData<>();

    public CategoryViewModel(@NonNull Application application) {
        super(application);

        repository = new CategoryRepository(application);
    }

    public LiveData<List<Category>> getAllComment() {
        repository.getAllCategory().observeForever(categories::setValue);
        return categories;
    }
}

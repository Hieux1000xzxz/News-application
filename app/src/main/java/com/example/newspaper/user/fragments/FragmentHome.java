package com.example.newspaper.user.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newspaper.admin.activities.AdminActivity;
import com.example.newspaper.auth.LoginActivity;
import com.example.newspaper.R;
import com.example.newspaper.user.activities.SearchActivity;
import com.example.newspaper.auth.UpdateInforActivity;
import com.example.newspaper.common.database.DatabaseHandler;
import com.example.newspaper.user.adapters.ArticleRecycleViewAdapter;
import com.example.newspaper.user.view_items.ArticleViewItem;
import com.example.newspaper.user.view_models.ArticleViewModel;

import java.util.ArrayList;
import java.util.List;

public class FragmentHome extends Fragment {
    ArticleViewModel articleViewModel;
    ArticleRecycleViewAdapter adapter;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DatabaseHandler db = DatabaseHandler.getInstance(requireContext());

        SharedPreferences prefs = requireContext().getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String role = prefs.getString("role", "user");

        if(role.equals("ADMIN")){
            startActivity(new Intent(requireActivity(), AdminActivity.class));
            requireActivity().finish();
        }else{
            recyclerView = view.findViewById(R.id.list_blogs);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new ArticleRecycleViewAdapter(new ArrayList<>());
            recyclerView.setAdapter(adapter);

            View accountBtn = view.findViewById(R.id.accountBtn);
            accountBtn.setOnClickListener(v -> {
                boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);
                if (isLoggedIn) {
                    long userId = prefs.getLong("userId", -1);
                    if (userId != -1) {
                        Intent intent = new Intent(requireContext(), UpdateInforActivity.class);
                        intent.putExtra("USER_ID", (int) userId);
                        startActivity(intent);
                    }else {
                        Toast.makeText(getContext(), "Có lỗi khi lấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                    }
                    navigate(view, R.id.accountBtn, UpdateInforActivity.class);

                } else {
                    Toast.makeText(getContext(), "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(requireContext(), LoginActivity.class));
                }
            });

            navigate(view, R.id.search_box, SearchActivity.class);

            articleViewModel = new ViewModelProvider(requireActivity()).get(ArticleViewModel.class);

            articleViewModel.loadArticles(10, 0);
            articleViewModel.getArticles().observe(getViewLifecycleOwner(), articles -> {
                List<ArticleViewItem> items = new ArrayList<>();
                articles.forEach(a -> items.add(new ArticleViewItem(a, ArticleViewItem.TypeDisplay.MAIN)));
                adapter.setArticles(items);
            });
        }
    }

    public void navigate(View parentView, int viewId, Class<?> targetActivity) {
        View button = parentView.findViewById(viewId);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), targetActivity);
            startActivity(intent);
        });
    }
}

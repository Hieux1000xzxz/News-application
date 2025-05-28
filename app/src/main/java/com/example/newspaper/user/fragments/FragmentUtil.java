package com.example.newspaper.user.fragments;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.GONE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.newspaper.R;
import com.example.newspaper.auth.LoginActivity;
import com.example.newspaper.auth.UpdateInforActivity;
import com.example.newspaper.user.view_models.UserViewModel;
import com.example.newspaper.user.activities.HistoryActivity;
import com.example.newspaper.user.activities.NotificationActivity;
import com.example.newspaper.user.activities.SettingActivity;

public class FragmentUtil extends Fragment {
    private TextView fullName, email;
    private UserViewModel viewModel;
    private ImageView avatar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_util, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        navigate(view, R.id.setting_icon, SettingActivity.class);
        navigate(view, R.id.notification_icon, NotificationActivity.class);

        fullName = view.findViewById(R.id.full_name);
        email = view.findViewById(R.id.email);
        avatar = view.findViewById(R.id.avatar);

        SharedPreferences prefs = getActivity().getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = (int) prefs.getLong("userId", -1);

        if(userId != -1){
            viewModel.getUserById(userId).observe(requireActivity(), user -> {
                fullName.setText(user.getFullName());
                email.setText(user.getEmail());
                if(user.getAvatarUrl() != null){
                    Glide.with(requireActivity())
                            .load(user.getAvatarUrl())
                            .apply(new RequestOptions().circleCrop())
                            .into(avatar);
                }
            });

            view.findViewById(R.id.user_detail).setOnClickListener(v -> {
                startActivity(new Intent(requireActivity(), UpdateInforActivity.class));
            });
        }else{
            fullName.setText("Đăng nhập");
            email.setVisibility(GONE);

            view.findViewById(R.id.user_detail).setOnClickListener(v -> {
                startActivity(new Intent(requireActivity(), LoginActivity.class));
            });
        }

        view.findViewById(R.id.saved_icon).setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), HistoryActivity.class);
            intent.putExtra("type", "saved");
            startActivity(intent);
        });

        view.findViewById(R.id.history_icon).setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), HistoryActivity.class);
            intent.putExtra("type", "history");
            startActivity(intent);
        });
    }

    public void navigate(View parentView, int viewId, Class<?> targetActivity) {
        View button = parentView.findViewById(viewId);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), targetActivity);
            startActivity(intent);
        });
    }
}

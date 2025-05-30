package com.example.newspaper.admin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.newspaper.R;
import com.example.newspaper.common.models.User;

import java.util.List;

public class UserManagerAdapter extends ArrayAdapter<User> {
    public UserManagerAdapter(Context context, List<User> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_admin_user, parent, false);
        }

        User user = getItem(position);

        TextView tvUsername = convertView.findViewById(R.id.tvUsername);
        TextView tvEmail = convertView.findViewById(R.id.tvEmail);
        TextView tvRole = convertView.findViewById(R.id.tvRole);

        tvUsername.setText(user.getFullName());
        tvEmail.setText(user.getEmail());
        tvRole.setText(user.getRole());

        return convertView;
    }
}

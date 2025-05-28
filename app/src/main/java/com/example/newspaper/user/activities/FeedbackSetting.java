package com.example.newspaper.user.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.newspaper.R;

public class FeedbackSetting extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.setting_feedback);

        findViewById(R.id.back_icon).setOnClickListener(v -> {
            finish();
        });
    }
}

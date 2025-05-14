package com.example.newspaper;

import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.newspaper.utils.DataInitializer;
import com.example.newspaper.views.panels.RootNavigationBarPanel;
import com.realgear.multislidinguppanel.MultiSlidingPanelAdapter;
import com.realgear.multislidinguppanel.MultiSlidingUpPanelLayout;
import com.realgear.multislidinguppanel.PanelStateListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private DataInitializer dataInitializer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Khởi tạo dữ liệu
        initializeData();

        MultiSlidingUpPanelLayout panelLayout = findViewById(R.id.root_sliding_up_panel);

        List<Class<?>> items = new ArrayList<>();

        items.add(RootNavigationBarPanel.class);

        panelLayout.setPanelStateListener(new PanelStateListener(panelLayout) {});

        panelLayout.setAdapter(new MultiSlidingPanelAdapter(this, items));
    }

    private void initializeData() {
        try {
            dataInitializer = new DataInitializer(this);
            dataInitializer.initializeData();
            Log.i(TAG, "Đã bắt đầu khởi tạo dữ liệu");
        } catch (Exception e) {
            Log.e(TAG, "Lỗi khi khởi tạo dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dataInitializer != null) {
            dataInitializer.close();
            Log.i(TAG, "Đã đóng DataInitializer");
        }
    }
}
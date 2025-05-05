package com.example.newsapplication;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapplication.Adapter.NotificationAdapter;
import com.example.newsapplication.Model.Notification;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity implements NotificationAdapter.OnNotificationClickListener {

    private RecyclerView recyclerViewNotifications;
    private NotificationAdapter adapter;
    private List<Notification> notificationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);

        // Initialize views
        recyclerViewNotifications = findViewById(R.id.recyclerViewNotifications);
        ImageButton btnBack = findViewById(R.id.btnBack);
        ImageButton btnMore = findViewById(R.id.btnMore);

        // Set up click listeners
        btnBack.setOnClickListener(v -> finish());
        btnMore.setOnClickListener(v -> Toast.makeText(this, "More options", Toast.LENGTH_SHORT).show());

        // Initialize notification list
        initNotificationList();

        // Set up RecyclerView
        recyclerViewNotifications.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotificationAdapter(this, notificationList, this);
        recyclerViewNotifications.setAdapter(adapter);
    }

    private void initNotificationList() {
        notificationList = new ArrayList<>();

        // Add sample notifications (as seen in the example)
        notificationList.add(new Notification(R.drawable.img_news1,
                "Hôm nay Tổng Bí thư Tô Lâm và Phu nhân lên đường công du 4 nước",
                "1 giờ trước",
                false));

        notificationList.add(new Notification(R.drawable.img_news1,
                "Chuyên gia nêu tiềm năng của TPHCM mới sẽ xứng tầm với Singapore, Thượng Hải",
                "2 giờ trước",
                false));

        notificationList.add(new Notification(R.drawable.img_news1,
                "Bộ Tài chính là bị hại trong vụ chuyển giao khu \"đất vàng\" 132 Bến Vân Đồn",
                "3 giờ trước",
                false));

        notificationList.add(new Notification(R.drawable.img_news1,
                "Khai mạc kỳ họp thứ 9, Quốc hội bắt đầu bàn sửa Hiến pháp 2013",
                "3 giờ trước",
                false));

        notificationList.add(new Notification(R.drawable.img_news1,
                "Thời tiết ngày mai 05/05\nThành phố Hà Nội: u ám, nhiệt độ từ 26°C đến 36°C, khả năng có mưa cao nhất 3%.",
                "13 giờ trước",
                false));

        notificationList.add(new Notification(R.drawable.img_news1,
                "Đoàn QĐND Việt Nam tham gia buổi sơ duyệt lễ duyệt binh trên Quảng trường Đỏ",
                "13 giờ trước",
                false));

        notificationList.add(new Notification(R.drawable.img_news1,
                "Ông Putin: Hy vọng Nga không phải sử dụng vũ khí hạt nhân ở Ukraine",
                "14 giờ trước",
                false));

        notificationList.add(new Notification(R.drawable.img_news1,
                "Công an làm việc với mẹ nữ sinh 14 tuổi tử vong do tai nạn giao thông",
                "17 giờ trước",
                false));

        notificationList.add(new Notification(R.drawable.img_news1,
                "Vì sao không bầu mà chỉ định chủ tịch các tỉnh, thành sau sáp nhập?",
                "18 giờ trước",
                false));
    }

    @Override
    public void onNotificationClick(int position) {
        Notification notification = notificationList.get(position);
        notification.setRead(true);
        adapter.notifyItemChanged(position);

        // Here you would typically open the notification content
        Toast.makeText(this, "Opened: " + notification.getContent(), Toast.LENGTH_SHORT).show();
    }
}

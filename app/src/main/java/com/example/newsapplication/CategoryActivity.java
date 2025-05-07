package com.example.newsapplication;

import static com.example.newsapplication.R.id.newsRecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapplication.Adapter.NewsAdapter;
import com.example.newsapplication.Model.NewsItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private RecyclerView newsRecyclerView;
    private NewsAdapter newsAdapter;
    private TabLayout tabLayout;
    private ImageButton menuButton;
    private BottomSheetDialog categoriesDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        // Khởi tạo RecyclerView
        newsRecyclerView = findViewById(R.id.newsRecyclerView);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Tạo dữ liệu tin tức mẫu
        List<NewsItem> newsList = createSampleNews();

        // Khởi tạo Adapter
        newsAdapter = new NewsAdapter(newsList);
        newsRecyclerView.setAdapter(newsAdapter);

        // Cài đặt Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            // Xử lý khi click vào các mục trong bottom navigation
            int itemId = item.getItemId();
            if (itemId == R.id.menu_home) {
                // Xử lý khi click vào Home
                return true;
            } else if (itemId == R.id.menu_category) {
                // Xử lý khi click vào Category
                return true;
            } else if (itemId == R.id.menu_video) {
                // Xử lý khi click vào Video
                return true;
            } else if (itemId == R.id.menu_ai) {
                // Xử lý khi click vào AI
                return true;
            } else if (itemId == R.id.menu_utilities) {
                // Xử lý khi click vào Utilities
                return true;
            }
            return false;
        });

        // Cài đặt TabLayout
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Xử lý khi tab được chọn
                // Trong ứng dụng thực tế, bạn sẽ cần tải dữ liệu tương ứng
                loadNewsByCategory(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Không làm gì
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Không làm gì
            }
        });

        // Thêm xử lý cho nút menu
        menuButton = findViewById(R.id.menuButton);
        if (menuButton != null) {
            menuButton.setOnClickListener(v -> showCategoriesDialog());
        }

        // Khởi tạo categories dialog
        setupCategoriesDialog();
    }

    private void setupCategoriesDialog() {
        categoriesDialog = new BottomSheetDialog(this);
        View dialogView = getLayoutInflater().inflate(R.layout.categories_dialog, null);
        categoriesDialog.setContentView(dialogView);

        // Tìm và thiết lập sự kiện cho nút đóng
        ImageButton closeButton = dialogView.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> categoriesDialog.dismiss());

        // Thiết lập sự kiện click cho các chuyên mục
        setupCategoryClickListeners(dialogView);
    }

    private void setupCategoryClickListeners(View dialogView) {
        // Chuyên mục Tin mới nhất
        View latestNewsCategory = dialogView.findViewById(R.id.latestNewsCategory);
        latestNewsCategory.setOnClickListener(v -> {
            tabLayout.getTabAt(0).select(); // Chọn tab "Mới nhất"
            categoriesDialog.dismiss();
        });

        // Chuyên mục Điểm tin nổi bật
        View featuredNewsCategory = dialogView.findViewById(R.id.featuredNewsCategory);
        featuredNewsCategory.setOnClickListener(v -> {
            tabLayout.getTabAt(1).select(); // Chọn tab "Điểm tin"
            categoriesDialog.dismiss();
        });

        // Chuyên mục Kinh doanh
        View businessCategory = dialogView.findViewById(R.id.businessCategory);
        businessCategory.setOnClickListener(v -> {
            tabLayout.getTabAt(3).select(); // Chọn tab "Kinh doanh"
            categoriesDialog.dismiss();
        });

        // Thêm listener cho các chuyên mục khác
        setupMoreCategories(dialogView);
    }

    private void setupMoreCategories(View dialogView) {
        // Xã hội
        View socialCategory = dialogView.findViewById(R.id.socialCategory);
        if (socialCategory != null) {
            socialCategory.setOnClickListener(v -> {
                // Có thể thêm tab mới hoặc chuyển đến Activity khác
                loadNewsByCategory("social");
                categoriesDialog.dismiss();
            });
        }

        // Thế giới
        View worldCategory = dialogView.findViewById(R.id.worldCategory);
        if (worldCategory != null) {
            worldCategory.setOnClickListener(v -> {
                loadNewsByCategory("world");
                categoriesDialog.dismiss();
            });
        }

        // Giải trí
        View entertainmentCategory = dialogView.findViewById(R.id.entertainmentCategory);
        if (entertainmentCategory != null) {
            entertainmentCategory.setOnClickListener(v -> {
                loadNewsByCategory("entertainment");
                categoriesDialog.dismiss();
            });
        }

        // Bất động sản
        View realEstateCategory = dialogView.findViewById(R.id.realEstateCategory);
        if (realEstateCategory != null) {
            realEstateCategory.setOnClickListener(v -> {
                loadNewsByCategory("realestate");
                categoriesDialog.dismiss();
            });
        }
    }

    private void showCategoriesDialog() {
        if (categoriesDialog != null) {
            categoriesDialog.show();
        }
    }

    private void loadNewsByCategory(int categoryPosition) {
        // Xử lý tải dữ liệu theo tab đã chọn
        String category;

        switch (categoryPosition) {
            case 0:
                category = "latest";
                break;
            case 1:
                category = "featured";
                break;
            case 2:
                category = "personal";
                break;
            case 3:
                category = "business";
                break;
            default:
                category = "latest";
        }

        loadNewsByCategory(category);
    }

    private void loadNewsByCategory(String category) {
        // Trong ứng dụng thực tế, bạn sẽ tải dữ liệu từ API hoặc database
        List<NewsItem> filteredNews = new ArrayList<>();

        // Mô phỏng việc lọc tin tức theo chuyên mục
        if (category.equals("latest")) {
            filteredNews = createSampleNews(); // Lấy tất cả tin
        } else {
            // Lọc tin theo chuyên mục
            for (NewsItem item : createSampleNews()) {
                if ((category.equals("social") && item.getCategory().equals("Xã hội")) ||
                        (category.equals("world") && item.getCategory().equals("Thế giới")) ||
                        (category.equals("law") && item.getCategory().equals("Pháp luật"))) {
                    filteredNews.add(item);
                }
            }
        }

        // Cập nhật RecyclerView
        newsAdapter.updateNewsList(filteredNews);
    }

    private List<NewsItem> createSampleNews() {
        List<NewsItem> newsList = new ArrayList<>();

        newsList.add(new NewsItem(
                "21 phút trước",
                "Pháp luật",
                "Ba mẹ con tử vong trong căn nhà khóa cửa",
                "(Dân trí) - Ba mẹ con ở tỉnh Gia Lai được phát hiện đã tử vong trong căn nhà khóa cửa. Công an đang điều tra nguyên nhân vụ việc."
        ));

        newsList.add(new NewsItem(
                "26 phút trước",
                "Xã hội",
                "Vụ 3 người tử vong trong khách sạn ở Nha Trang: Người phụ nữ đang mang thai",
                "(Dân trí) - Gia đình đã chôn cất 2 mẹ con trong vụ 3 người tử vong tại khách sạn ở Nha Trang. Theo người thân của nữ nạn nhân, người này đang mang thai hơn 3 tháng."
        ));

        newsList.add(new NewsItem(
                "31 phút trước",
                "Xã hội",
                "Ô tô chở 20 người gặp tai nạn trên quốc lộ",
                "(Dân trí) - Xe tải đang lưu thông trên quốc lộ 20 ở Lâm Đồng, bất ngờ va chạm ô tô chở khách. Vụ tai nạn làm nhiều người hoảng loạn, giao thông qua khu vực bị ách tắc."
        ));

        newsList.add(new NewsItem(
                "34 phút trước",
                "Thế giới",
                "Ukraine tuyên bố lần đầu bắn hạ tiêm kích Nga từ xuồng không người lái",
                "(Dân trí) - Ukraine cho biết đã bắn rơi tiêm kích Nga trên biển từ xuồng không người lái."
        ));

        return newsList;
    }
}
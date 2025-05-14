package com.example.newspaper.utils;

import android.content.Context;
import android.util.Log;

import com.example.newspaper.database.DatabaseHandler;
import com.example.newspaper.models.Article;
import com.example.newspaper.models.Category;
import com.example.newspaper.database.repositories.ArticleRepository;
import com.example.newspaper.database.repositories.CategoryRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Lớp tiện ích để kiểm tra và lấy dữ liệu từ database
 */
public class DataInitializer {
    private static final String TAG = "DataInitializer";
    private final DatabaseHandler database;
    private final ExecutorService executorService;
    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;

    public DataInitializer(Context context) {
        database = DatabaseHandler.getInstance(context);
        executorService = Executors.newSingleThreadExecutor();
        articleRepository = new ArticleRepository(context);
        categoryRepository = new CategoryRepository(context);
    }

    public void initializeData() {
        executorService.execute(() -> {
            try {
                Log.d(TAG, "Bắt đầu khởi tạo dữ liệu");

                // Bước 1: Xóa tất cả các danh mục (sẽ tự động xóa các bài viết liên quan nhờ CASCADE)
                clearAllCategories();
                Log.d(TAG, "Đã xóa dữ liệu cũ");

                // Bước 2: Tạo lại danh mục mặc định
                List<Category> categories = createDefaultCategories();
                Log.d(TAG, "Đã tạo danh mục mặc định");

                // Đợi một khoảng thời gian để đảm bảo danh mục được lưu vào database
                Thread.sleep(800);

                // Bước 3: Lấy danh sách danh mục từ database với ID
                List<Category> categoriesWithId = fetchAllCategories();
                if (categoriesWithId.isEmpty()) {
                    Log.e(TAG, "Không thể lấy danh mục từ database sau khi tạo");
                    return;
                }

                Log.d(TAG, "Đã lấy được " + categoriesWithId.size() + " danh mục từ database");

                // Bước 4: Tạo bài viết cho mỗi danh mục
                createArticlesForAllCategories(categoriesWithId);
                Log.d(TAG, "Đã tạo bài viết cho tất cả danh mục");

                // Đợi một chút để đảm bảo dữ liệu được lưu vào database
                Thread.sleep(1000);

                Log.i(TAG, "Khởi tạo dữ liệu thành công");
            } catch (Exception e) {
                Log.e(TAG, "Lỗi khi khởi tạo dữ liệu: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    private void clearAllCategories() {
        try {
            CountDownLatch latch = new CountDownLatch(1);
            categoryRepository.deleteAll();
            latch.await(3, TimeUnit.SECONDS);
            Log.i(TAG, "Đã xoá tất cả danh mục và bài viết liên quan");
        } catch (InterruptedException e) {
            Log.e(TAG, "Lỗi khi xóa danh mục: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private List<Category> createDefaultCategories() {
        List<Category> createdCategories = new ArrayList<>();
        LocalDate now = LocalDate.now();

        String[] categoryNames = {
                "Xã hội", "Thế giới", "Kinh doanh", "Giải trí", "Thể thao",
                "Công nghệ", "Giáo dục", "Sức khỏe", "Pháp luật"
        };

        String[] categoryDescriptions = {
                "Tin tức xã hội", "Tin tức thế giới", "Tin tức kinh doanh",
                "Tin tức giải trí", "Tin tức thể thao", "Tin tức công nghệ",
                "Tin tức giáo dục", "Tin tức sức khỏe", "Tin tức pháp luật"
        };

        for (int i = 0; i < categoryNames.length; i++) {
            Category category = new Category(null, categoryNames[i], categoryDescriptions[i], now);
            categoryRepository.insert(category);
            Log.d(TAG, "Đã thêm danh mục: " + categoryNames[i]);
            createdCategories.add(category);
        }

        return createdCategories;
    }

    private List<Category> fetchAllCategories() {
        final List<Category>[] result = new List[1];
        CountDownLatch latch = new CountDownLatch(1);

        categoryRepository.getAllCategories().observeForever(categories -> {
            if (categories != null && !categories.isEmpty()) {
                result[0] = categories;
                latch.countDown();
            }
        });

        try {
            boolean success = latch.await(5, TimeUnit.SECONDS);
            if (!success) {
                Log.e(TAG, "Hết thời gian chờ khi lấy dữ liệu danh mục");
            }
        } catch (InterruptedException e) {
            Log.e(TAG, "Lỗi khi chờ lấy danh mục: " + e.getMessage());
            Thread.currentThread().interrupt();
        }

        return result[0] != null ? result[0] : new ArrayList<>();
    }

    private void createArticlesForAllCategories(List<Category> categories) {
        int articlesPerCategory = 5;

        for (Category category : categories) {
            if (category.getId() != null) {
                createArticlesForCategory(category, 1, articlesPerCategory);
                Log.d(TAG, "Đã tạo " + articlesPerCategory + " bài viết cho danh mục: " + category.getName());
            } else {
                Log.e(TAG, "Danh mục " + category.getName() + " không có ID hợp lệ, bỏ qua việc tạo bài viết");
            }
        }
    }

    private void createArticlesForCategory(Category category, int startIndex, int endIndex) {
        Instant now = Instant.now();

        for (int i = startIndex; i <= endIndex; i++) {
            String title = "Bài viết " + i + " - " + category.getName();
            String summary = "Tóm tắt bài viết " + i + " thuộc danh mục " + category.getName();
            String content = "Nội dung chi tiết của bài viết " + i + " thuộc danh mục " + category.getName() +
                    ". Đây là một bài viết mẫu được tạo tự động để kiểm tra chức năng của ứng dụng.";

            Article article = new Article(
                    null,
                    title,
                    summary,
                    content,
                    "https://example.com/image" + i + ".jpg",
                    0,
                    now,
                    now,
                    category.getId(),  // Sử dụng ID của category đã lấy từ database
                    1  // Sử dụng ID mặc định cho user
            );

            articleRepository.insert(article);
            try {
                // Đợi một chút để tránh insert quá nhanh
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Log.e(TAG, "Bị gián đoạn khi đang tạo bài viết: " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

    public void close() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(2, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}
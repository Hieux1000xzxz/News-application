package com.example.newspaper.common.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.newspaper.common.database.dao.ArticleDao;
import com.example.newspaper.common.database.dao.CategoryDao;
import com.example.newspaper.common.database.dao.CommentDao;
import com.example.newspaper.common.database.dao.EmotionDao;
import com.example.newspaper.common.database.dao.NotificationDao;
import com.example.newspaper.common.database.dao.ReadHistoryDao;
import com.example.newspaper.common.database.dao.SavedArticleDao;
import com.example.newspaper.common.database.dao.SearchHistoryDao;
import com.example.newspaper.common.database.dao.UserDao;
import com.example.newspaper.common.models.Article;
import com.example.newspaper.common.models.Category;
import com.example.newspaper.common.models.Comment;
import com.example.newspaper.common.models.Emotion;
import com.example.newspaper.common.models.Notification;
import com.example.newspaper.common.models.ReadHistory;
import com.example.newspaper.common.models.SavedArticle;
import com.example.newspaper.common.models.SearchHistory;
import com.example.newspaper.common.models.User;
import com.example.newspaper.common.utils.Converters;
import com.example.newspaper.common.utils.EncryptionUtil;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
        entities = {
                Article.class, Comment.class, Emotion.class, Notification.class, ReadHistory.class,
                SearchHistory.class, User.class, Category.class, SavedArticle.class
        },
        version = 4
)
@TypeConverters(Converters.class)
public abstract class DatabaseHandler extends RoomDatabase {

    private static DatabaseHandler instance;
    private static Context context;

    public abstract ArticleDao getArticleDao();
    public abstract CategoryDao getCategoryDao();
    public abstract UserDao getUserDao();
    public abstract SearchHistoryDao getSearchHistoryDao();
    public abstract EmotionDao getEmotionDao();
    public abstract CommentDao getCommentDao();
    public abstract SavedArticleDao getSavedArticleDao();
    public abstract ReadHistoryDao getReadHistoryDao();
    public abstract NotificationDao getNotificationDao();

    public static DatabaseHandler getInstance(Context context){
        if(instance == null){
            DatabaseHandler.context = context;
            instance = Room.databaseBuilder(context.getApplicationContext(), DatabaseHandler.class, "news_paper.db")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    .build();
        }

        return instance;
    };

    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(4);

    public static RoomDatabase.Callback roomCallBack = new Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase sqd){
            super.onCreate(sqd);

            Executors.newSingleThreadExecutor().execute(() -> {
                Log.d("ROOM_CALLBACK", "Inserting categories...");
                // Lấy instance của database
                DatabaseHandler db = getInstance(context);
                db.getCategoryDao().insert(Category.builder()
                                .name("Kinh doanh")
                                .description("Chuyên về các tin tức, xu hướng và phân tích liên quan đến lĩnh vực kinh tế, doanh nghiệp, và các thị trường tài chính. Các bài viết có thể đề cập đến sự thay đổi trong nền kinh tế, các chiến lược kinh doanh, quản lý tài chính, cổ phiếu và đầu tư.")
                        .build());

                db.getCategoryDao().insert(Category.builder()
                        .name("Xã hội")
                        .description("Tập trung vào các vấn đề xã hội, bao gồm các câu chuyện về cộng đồng, các vấn đề xã hội hiện tại, hành động của chính phủ và các tổ chức đối với các nhóm yếu thế, và các chiến lược để giải quyết các vấn đề xã hội.")
                        .build());

                db.getCategoryDao().insert(Category.builder()
                        .name("Thế giới")
                        .description("Tin tức quốc tế về các sự kiện lớn diễn ra trên toàn cầu, bao gồm các cuộc xung đột, hội nghị quốc tế, thỏa thuận thương mại và các sự kiện quan trọng trong chính trị và ngoại giao toàn cầu.")
                        .build());

                db.getCategoryDao().insert(Category.builder()
                        .name("Giải trí")
                        .description("Các tin tức liên quan đến âm nhạc, điện ảnh, nghệ thuật và các hoạt động giải trí khác. Thể loại này bao gồm các thông tin về bộ phim mới, album nhạc, các sự kiện văn hóa và các nhân vật nổi tiếng trong giới nghệ sĩ.")
                        .build());

                db.getCategoryDao().insert(Category.builder()
                        .name("Bất động sản")
                        .description("Chuyên cung cấp thông tin về thị trường bất động sản, giá trị đất đai, các dự án mới, các thay đổi trong chính sách và xu hướng đầu tư trong lĩnh vực bất động sản.")
                        .build());

                db.getCategoryDao().insert(Category.builder()
                        .name("Thể thao")
                        .description("Tất cả các tin tức về các môn thể thao, các giải đấu, thành tích của các vận động viên, cũng như các sự kiện thể thao lớn trên toàn thế giới. Chuyên mục này bao gồm bóng đá, tennis, bóng rổ, thể dục thể thao và nhiều môn thể thao khác.")
                        .build());

                db.getCategoryDao().insert(Category.builder()
                        .name("Sức khỏe")
                        .description("Tin tức và thông tin về các vấn đề sức khỏe, bao gồm các bệnh tật, phương pháp điều trị, sức khỏe cộng đồng, chế độ ăn uống, lối sống lành mạnh và các thông tin khoa học liên quan đến y tế.")
                        .build());

                db.getCategoryDao().insert(Category.builder()
                        .name("Nội vụ")
                        .description("Đề cập đến các vấn đề trong nội bộ của một quốc gia hoặc chính quyền, bao gồm chính sách công, hành động của chính phủ, các vấn đề về an ninh, pháp luật, và các vấn đề quản lý công.")
                        .build());

                db.getCategoryDao().insert(Category.builder()
                        .name("Nhân ái")
                        .description("Các câu chuyện về lòng từ thiện, các hoạt động thiện nguyện, các tổ chức nhân đạo và những hành động giúp đỡ cộng đồng. Thể loại này chú trọng đến những hành động vì cộng đồng và các câu chuyện đầy cảm hứng.")
                        .build());

                db.getCategoryDao().insert(Category.builder()
                        .name("Xe ++")
                        .description("Tin tức về các phương tiện giao thông, xe hơi, xe máy, cũng như các công nghệ mới trong ngành công nghiệp ô tô và giao thông. Thể loại này cũng có thể bao gồm các xu hướng mới về phương tiện giao thông tự động, xe điện và các dự án giao thông lớn.")
                        .build());

                db.getCategoryDao().insert(Category.builder()
                        .name("Công nghệ")
                        .description("Tin tức về công nghệ mới nhất, các sản phẩm công nghệ, phát minh sáng chế, tiến bộ trong khoa học kỹ thuật và các ứng dụng công nghệ trong các lĩnh vực khác nhau của đời sống, từ điện thoại thông minh đến trí tuệ nhân tạo.")
                        .build());

                db.getCategoryDao().insert(Category.builder()
                        .name("Giáo dục")
                        .description("Tin tức về các hệ thống giáo dục, phương pháp giảng dạy, các chương trình học và xu hướng giáo dục mới, cũng như các câu chuyện về học bổng, trường học và các thay đổi trong ngành giáo dục.")
                        .build());

                db.getCategoryDao().insert(Category.builder()
                        .name("Việc làm")
                        .description("Tin tức và thông tin liên quan đến thị trường việc làm, cơ hội nghề nghiệp, kỹ năng nghề, các ngành nghề phát triển và những thay đổi trong yêu cầu công việc. Chuyên mục này cũng có thể bao gồm các lời khuyên về việc tìm kiếm việc làm, phát triển sự nghiệp và môi trường làm việc.")
                        .build());

                db.getCategoryDao().insert(Category.builder()
                        .name("Pháp luật")
                        .description("Cung cấp thông tin về các quy định pháp lý, luật pháp mới, các vụ án lớn, và các diễn biến quan trọng trong ngành tư pháp. Chuyên mục này tập trung vào những thay đổi trong hệ thống pháp luật và các vấn đề pháp lý quan trọng.")
                        .build());

                // tao du lieu cho bang nguoi dung
                db.getUserDao().insert(User.builder()
                        .fullName("Nguyễn Văn Sung")
                        .email("vsung2608@gmail.com")
                        .phone("0964034162")
                        .avatarUrl("https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D")
                        .createdAt(Date.from(Instant.now()))
                        .isMale(true)
                        .passwordHash(EncryptionUtil.hashPassword("vsung2608"))
                        .city("Thuận Thành - Bắc Ninh")
                        .role("admin")
                        .build());

                db.getUserDao().insert(User.builder()
                        .fullName("Hoàng Bá Minh")
                        .email("minhhb@gmail.com")
                        .phone("06413165481")
                        .avatarUrl("https://untitledui.com/images/avatars/orlando-diggs")
                        .createdAt(Date.from(Instant.now()))
                        .isMale(true)
                        .passwordHash(EncryptionUtil.hashPassword("minhhb123"))
                        .city("Hải Phòng")
                        .role("admin")
                        .build());

                db.getUserDao().insert(User.builder()
                        .fullName("Đỗ Hoài Nam")
                        .email("namhd@gmail.com")
                        .phone("012345678")
                        .avatarUrl("https://untitledui.com/images/avatars/joshua-wilson")
                        .createdAt(Date.from(Instant.now()))
                        .isMale(true)
                        .passwordHash(EncryptionUtil.hashPassword("vsung2608"))
                        .city("Thuận Thành - Bắc Ninh")
                        .role("admin")
                        .build());

                // tao du lieu cho bang bai viet
                db.getArticleDao().insert(Article.builder()
                        .title("Mỹ - Trung liệu đã chính thức \"tháo ngòi\" thương chiến?")
                        .summary("(Dân trí) - Sau các cuộc đàm phán vào cuối tuần trước, Mỹ và Trung Quốc đã đồng ý giảm thuế quan trong 90 ngày. Liệu cuộc gặp giữa 2 nền kinh tế lớn nhất thế giới có mang lại sự khởi sắc cho thị trường toàn cầu?")
                        .categoryId(1)
                        .thumbnailUrl("https://cdnphoto.dantri.com.vn/gxTLQVyv-It0YTA4GYBa2_Dyf-A=/2025/04/27/gold-1745760782075.jpg")
                        .userId(1)
                        .viewCount(122)
                        .publishedAt(Instant.now())
                        .build());

                db.getArticleDao().insert(Article.builder()
                        .title("Cơn sốt vàng: Lịch sử có lặp lại trong thời đại số?")
                        .summary("Giữa lúc giá vàng liên tiếp trồi, sụt, phá vỡ các kỷ lục và nền kinh tế toàn cầu đầy biến động, liệu điều gì sẽ xảy ra với thị trường vàng?")
                        .categoryId(1)
                        .thumbnailUrl("https://cdnphoto.dantri.com.vn/sC5WCJ8qk0SXRa0_OZhkVYTAoOI=/thumb_w/1360/2025/05/12/trump-my-trung-1747036683651.jpg")
                        .userId(1)
                        .viewCount(122)
                        .publishedAt(Instant.now().minus(15, ChronoUnit.MINUTES))
                        .build());

                db.getArticleDao().insert(Article.builder()
                        .title("Nhiều chủ tịch tỉnh bị kiện nhưng không đến tòa")
                        .summary("(Dân trí) - Năm 2024, cả nước thụ lý hơn 13.000 vụ án hành chính, trong đó hơn 11.000 vụ kiện chủ tịch UBND tỉnh và quận, huyện… nhưng chỉ giải quyết được hơn 8.000 vụ, theo đại biểu Nguyễn Hữu Chính.")
                        .categoryId(2)
                        .thumbnailUrl("https://cdnphoto.dantri.com.vn/WfoXhi-cwgo3JE-_GzDO5bem-lU=/thumb_w/1360/2025/05/12/nguyenhuuching-1747043749007.jpg")
                        .userId(1)
                        .viewCount(200)
                        .publishedAt(Instant.now().minus(20, ChronoUnit.MINUTES))
                        .build());

                db.getArticleDao().insert(Article.builder()
                        .title("Ấn Độ - Pakistan tổn thất lớn sau 4 ngày đối đầu ở điểm nóng Kashmir")
                        .summary("(Dân trí) - Cuộc xung đột vài ngày qua giữa Ấn Độ và Pakistan không chỉ gây thiệt hại về tài sản mà còn gây ra chấn thương tâm lý và trì trệ kinh tế.")
                        .categoryId(3)
                        .thumbnailUrl("https://cdnphoto.dantri.com.vn/FNjMHhmokfw9dvQf08iCfEvUAVY=/thumb_w/1360/2025/05/12/an-domehr-1747036751617.jpg")
                        .userId(1)
                        .viewCount(150)
                        .publishedAt(Instant.now().minus(30, ChronoUnit.MINUTES))
                        .build());

                db.getArticleDao().insert(Article.builder()
                        .title("Diện váy dài mùa hè: Bí quyết dành cho quý cô lãng mạn và duyên dáng")
                        .summary("Nếu bạn là người lãng mạn, đừng bỏ qua chiếc váy dài màu trắng. Được gọi là \"váy tình đầu\", loại trang phục này mang đến vẻ đẹp trong sáng, dịu dàng và dễ phối đồ.")
                        .categoryId(4)
                        .thumbnailUrl("https://cdnphoto.dantri.com.vn/6lyT1yUlLd380UBhJxXdVocJJjI=/zoom/504_336/2025/05/09/e654bbc51112415f958083c54759db9e-crop-crop-1746784649007.jpeg")
                        .userId(1)
                        .viewCount(150)
                        .publishedAt(Instant.now().minus(90, ChronoUnit.MINUTES))
                        .build());

                db.getArticleDao().insert(Article.builder()
                        .title("Khu tập thể Vĩnh Hồ có thể được xây các tòa nhà cao 40 tầng")
                        .summary("Theo quy hoạch 36 tòa nhà tại khu tập thể Vĩnh Hồ (Đống Đa, TP Hà Nội) được xây dựng thành các tòa nhà cao 40 tầng để tái định cư tại chỗ cho người dân và mục đích thương mại của nhà đầu tư.")
                        .categoryId(5)
                        .thumbnailUrl("https://cdnphoto.dantri.com.vn/wkvLYKvEbihDqYtHlsP-IPktU-Q=/zoom/504_336/2025/05/09/tap20the20v20ho-crop-1746762810551.jpeg")
                        .userId(1)
                        .viewCount(150)
                        .publishedAt(Instant.now().minus(110, ChronoUnit.MINUTES))
                        .build());

                db.getArticleDao().insert(Article.builder()
                        .title("Alcaraz, Zverev vững vàng tiến bước tại Italian Open")
                        .summary("Carlos Alcaraz đã đánh bại Laslo Djere với tỷ số 7-6(2), 6-2 ở trận đấu tại vòng 3 Italian Open vào rạng sáng 12/5, qua đó giành quyền vào thi đấu vòng 4 với Karen Khachanov.")
                        .categoryId(6)
                        .thumbnailUrl("https://cdnphoto.dantri.com.vn/XwHYVswlnyz8TvlQRLx52_7nRjU=/zoom/504_336/2025/05/12/alcaraz-crop-1747010367008.jpeg")
                        .userId(1)
                        .viewCount(150)
                        .publishedAt(Instant.now().minus(111, ChronoUnit.MINUTES))
                        .build());

                db.getArticleDao().insert(Article.builder()
                        .title("Ghép gan cho bệnh nhi 8 tháng tuổi từ người chết não: Kỳ tích y học Việt")
                        .summary("Ngày 18/4 trở thành một dấu mốc đáng nhớ với Hệ thống Y tế Vinmec và ngành phẫu thuật ở Việt Nam khi nơi này thực hiện thành công ca ghép gan đặc biệt cho một bệnh nhi 8 tháng tuổi, nặng chỉ 6,5kg.")
                        .categoryId(7)
                        .thumbnailUrl("https://cdnphoto.dantri.com.vn/H1Pju25PzPRNQuzgs13_YgUbuYk=/zoom/504_336/2025/05/10/thumb-1746868326344.jpg")
                        .userId(1)
                        .viewCount(150)
                        .publishedAt(Instant.now().minus(4, ChronoUnit.HOURS))
                        .build());

                db.getArticleDao().insert(Article.builder()
                        .title("Lamine Yamal gây sốt khi ăn mừng chế giễu C.Ronaldo và Mbappe")
                        .summary("Lamine Yamal đã không ngần ngại châm chọc Real Madrid khi \"nhái\" theo cách ăn mừng của C.Ronaldo và Mbappe.")
                        .categoryId(6)
                        .thumbnailUrl("https://cdnphoto.dantri.com.vn/h1KyBOTO0anpC0dhWdz7hkJADVM=/zoom/1032_688/2025/05/12/goal-multiple-images-2-split-facebook324jpg-6820e6b6ebd80-crop-1747036062997.jpeg")
                        .userId(1)
                        .viewCount(150)
                        .publishedAt(Instant.now().minus(5, ChronoUnit.HOURS))
                        .build());

                db.getArticleDao().insert(Article.builder()
                        .title("Gục ngã trước Newcastle, Chelsea có nguy cơ mất suất dự Champions League")
                        .summary("Các bàn thắng của Sandro Tonali và Bruno Guimaraes đã mang về chiến thắng 2-0 cho Newcastle trước Chelsea ở vòng 36 Premier League, diễn ra vào tối 11/5.")
                        .categoryId(6)
                        .thumbnailUrl("https://cdnphoto.dantri.com.vn/_Mr0Yl5jocPVeetQ0aeSk5C64wY=/zoom/504_336/2025/05/11/newcastle-chelsea-crop-1746970962924.jpeg")
                        .userId(1)
                        .viewCount(150)
                        .publishedAt(Instant.now().minus(7, ChronoUnit.HOURS))
                        .build());

                db.getArticleDao().insert(Article.builder()
                        .title("Aqua Warriors và Marathon: Lần đầu tiên hai giải đấu tổ chức ở Quảng Bình")
                        .summary("Quảng Bình sẽ trở thành điểm đến thể thao hấp dẫn bậc nhất khi lần đầu tiên, hai giải đấu với các nội dung Aquathlon (2 môn phối hợp bơi và chạy) và Marathon - được tổ chức trong hai ngày liên tiếp, thứ 7 và chủ nhật (2-3/8).")
                        .categoryId(6)
                        .thumbnailUrl("https://cdnphoto.dantri.com.vn/g3_wFEj-rsXUSWqgGiapYCWau-g=/zoom/504_336/2025/05/12/4751652151222113069022199248098552508639494890n-crop-1747022022971.jpeg")
                        .userId(1)
                        .viewCount(150)
                        .publishedAt(Instant.now().minus(15, ChronoUnit.HOURS))
                        .build());

                db.getArticleDao().insert(Article.builder()
                        .title("C.Ronaldo có động thái bất ngờ, tương lai trở nên bấp bênh")
                        .summary("C.Ronaldo đã hoãn gia hạn hợp đồng với Al Nassr. Điều đó khiến tương lai của cầu thủ người Bồ Đào Nha trở nên bấp bênh.")
                        .categoryId(6)
                        .thumbnailUrl("https://cdnphoto.dantri.com.vn/KQxQoXajNb0f2mAdWBJfairEjl4=/zoom/504_336/2025/05/11/ronaldo-jpeg-1746931440-174693-8597-9760-1746931562-crop-1746959631333.jpeg")
                        .userId(1)
                        .viewCount(150)
                        .publishedAt(Instant.now().minus(20, ChronoUnit.HOURS))
                        .build());
            });
        };
    };

}

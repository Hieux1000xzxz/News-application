package com.example.newspaper.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.newspaper.database.dao.ArticleDao;
import com.example.newspaper.database.dao.ArticleWithCategoryDao;
import com.example.newspaper.database.dao.CategoryDao;
import com.example.newspaper.models.Article;
import com.example.newspaper.models.Category;
import com.example.newspaper.models.Comment;
import com.example.newspaper.models.Emotion;
import com.example.newspaper.models.Notification;
import com.example.newspaper.models.ReadHistory;
import com.example.newspaper.models.SearchHistory;
import com.example.newspaper.models.User;
import com.example.newspaper.utils.Converters;

@Database(
        entities = {
                Article.class, Comment.class, Emotion.class, Notification.class, ReadHistory.class,
                SearchHistory.class, User.class, Category.class
        },
        version = 1
)
@TypeConverters(Converters.class)
public abstract class DatabaseHandler extends RoomDatabase {

    private static DatabaseHandler instance;

    public abstract ArticleDao getArticleDao();

    public abstract ArticleWithCategoryDao getArticleWithCategoryDao();

    public abstract CategoryDao getCategoryDao();


    public static DatabaseHandler getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), DatabaseHandler.class, "news_paper_app_db")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    .build();
        }

        return instance;
    };

    public static RoomDatabase.Callback roomCallBack = new Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase sqd){
            super.onCreate(sqd);
        };
    };
}
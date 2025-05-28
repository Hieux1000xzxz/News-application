package com.example.newspaper.common.database.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.newspaper.common.database.DatabaseHandler;
import com.example.newspaper.common.database.dao.ArticleDao;
import com.example.newspaper.common.database.dao.NotificationDao;
import com.example.newspaper.common.database.dao.UserDao;
import com.example.newspaper.common.models.Article;
import com.example.newspaper.common.models.Notification;
import com.example.newspaper.common.pojo.ArticleWithCategory;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.Executors;

public class ArticleRepository {
    private ArticleDao articleDao;
    private UserDao userDao;
    private NotificationDao notificationDao;

    public ArticleRepository(Context context) {
        DatabaseHandler dbh = DatabaseHandler.getInstance(context);

        this.articleDao = dbh.getArticleDao();
        this.userDao = dbh.getUserDao();
        this.notificationDao = dbh.getNotificationDao();
    }

    public void insert(Article article) {
        Executors.newSingleThreadExecutor().execute(() -> {
            long articleId = articleDao.insert(article);

            List<Integer> userIds = userDao.getUserIds();

            Instant now = Instant.now();

            for (Integer userId : userIds) {
                Notification notification = new Notification();
                notification.setUserId(userId);
                notification.setArticleId((int) articleId);
                notification.setRead(false);
                notification.setCreatedAt(now);

                notificationDao.insert(notification);
            }
        });
    }

    public void update(Article article) {
        Executors.newSingleThreadExecutor().execute(() -> {
            this.articleDao.update(article);
        });
    }

    public void delete(Article article) {
        Executors.newSingleThreadExecutor().execute(() -> {
            this.articleDao.delete(article);
        });
    }

    public void deleteAll() {
        Executors.newSingleThreadExecutor().execute(() -> {
            this.articleDao.deleteAlls();
        });
    }

    public List<ArticleWithCategory> getPagedArticles(int limit, int offset) {
        return articleDao.getArticlesPaged(limit, offset);
    }

    public List<ArticleWithCategory> getArticleByCategoryId(int categoryId) {
        return articleDao.getArticleByCategoryId(categoryId);
    }

    public List<ArticleWithCategory> getArticleByIds(List<Integer> ids) {
        return articleDao.getArticlesByIds(ids);
    }

    public List<ArticleWithCategory> getRelatedArticle(int id) {
        return articleDao.getRelatedArticle(id);
    }

    public List<ArticleWithCategory> getArticleNewest() {
        return articleDao.getTodayArticleOrderByNewest();
    }

    public LiveData<List<Article>> getAll(){
        return articleDao.getAllArticle();
    };

    public LiveData<Article> getOne(int id){
        return articleDao.getOneArticleById(id);
    };

    public void increaseViewCount(int id){
        Executors.newSingleThreadExecutor().execute(() -> {
            this.articleDao.increaseViewCount(id);
        });
    }

    public ArticleWithCategory getArticleById(int id){
        return articleDao.getArticleById(id);
    }

    public List<ArticleWithCategory> getArticleByKeyword(String key1, String key2, String key3, String key4) {
        return articleDao.searchArticleByKeywords(key1, key2, key3, key4);
    }
}

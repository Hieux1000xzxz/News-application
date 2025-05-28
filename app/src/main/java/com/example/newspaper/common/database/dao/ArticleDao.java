package com.example.newspaper.common.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.newspaper.common.models.Article;
import com.example.newspaper.common.pojo.ArticleWithCategory;

import java.util.List;


@Dao
public interface ArticleDao {

    @Insert
    long insert(Article article);

    @Update
    void update(Article article);

    @Delete
    void delete(Article article);

    @Query("DELETE FROM article_table")
    void deleteAlls();

    @Query("SELECT * FROM article_table ORDER BY publishedAt DESC LIMIT :limit OFFSET :offset")
    List<ArticleWithCategory> getArticlesPaged(int limit, int offset);

    @Query("SELECT * FROM article_table WHERE id IN (:articleIds)")
    List<ArticleWithCategory> getArticlesByIds(List<Integer> articleIds);

    @Query("SELECT * FROM article_table WHERE categoryId = :categoryId")
    List<ArticleWithCategory> getArticleByCategoryId(int categoryId);

    @Query("SELECT * FROM article_table WHERE categoryId = :categoryId LIMIT 5")
    List<ArticleWithCategory> getRelatedArticle(int categoryId);

    @Query("SELECT * FROM article_table WHERE date(publishedAt) = date('now', 'localtime') ORDER BY publishedAt DESC")
    List<ArticleWithCategory> getTodayArticleOrderByNewest();

    @Query("SELECT * FROM article_table ORDER BY publishedAt DESC")
    LiveData<List<Article>> getAllArticle();

    @Query("SELECT * FROM article_table WHERE id = :id")
    ArticleWithCategory getArticleById(int id);

    @Query("SELECT * FROM article_table WHERE id = :id")
    LiveData<Article> getOneArticleById(int id);

    @Query("UPDATE article_table SET viewCount = viewCount + 1 WHERE id = :articleId")
    void increaseViewCount(int articleId);

    @Query("SELECT * FROM article_table WHERE " +
            "title LIKE :keyword1 OR title LIKE :keyword2 " +
            "OR title LIKE :keyword3 OR title LIKE :keyword4 " +
            "ORDER BY publishedAt DESC")
    List<ArticleWithCategory> searchArticleByKeywords(String keyword1, String keyword2, String keyword3, String keyword4);
}

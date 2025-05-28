package com.example.newspaper.common.pojo;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.newspaper.common.models.Article;
import com.example.newspaper.common.models.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleWithCategory {
    @Embedded
    public Article article;

    @Relation(
            parentColumn = "categoryId",
            entityColumn = "id"
    )
    public Category category;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        ArticleWithCategory that = (ArticleWithCategory) o;
        return article.equals(that.article) && category.equals(that.category);
    }

    @Override
    public int hashCode() {
        int result = article.hashCode();
        result = 31 * result + category.hashCode();
        return result;
    }
}

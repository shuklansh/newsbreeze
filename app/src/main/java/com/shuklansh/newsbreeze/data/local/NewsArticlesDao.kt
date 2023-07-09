package com.shuklansh.newsbreeze.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.shuklansh.newsbreeze.domain.data.Article


@Dao
interface NewsArticlesDao {

    @Upsert
    suspend fun bookmarkArticle( article: ArticleEntity )

    @Delete
    suspend fun deleteArticle( article: ArticleEntity )

    @Query("SELECT * FROM articleentity ORDER BY author")
    suspend fun getAllArticles() : List<ArticleEntity>

    @Query("SELECT * FROM articleentity ORDER BY publishedAt")
    suspend fun getAllArticlesByDate() : List<ArticleEntity>

    @Query("SELECT EXISTS(SELECT * FROM articleentity WHERE title = :title)")
    suspend fun isarticleintheDB(title : String) : Boolean

}
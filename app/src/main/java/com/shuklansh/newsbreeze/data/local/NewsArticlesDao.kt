package com.shuklansh.newsbreeze.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.shuklansh.newsbreeze.domain.data.Article
import com.shuklansh.newsbreeze.domain.data.NewsResponse


@Dao
interface NewsArticlesDao {

    @Upsert
    suspend fun bookmarkArticle( article: NewsArticlesEntity )

    @Delete
    suspend fun deleteArticle( article: NewsArticlesEntity )

    @Query("SELECT * FROM newsarticlesentity")
    suspend fun getAllArticles() : List<NewsArticlesEntity>

    @Query("SELECT EXISTS(SELECT * FROM newsarticlesentity WHERE article = :article)")
    suspend fun isarticleintheDB(article : Article) : Boolean

}
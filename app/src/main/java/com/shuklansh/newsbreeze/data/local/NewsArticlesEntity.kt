package com.shuklansh.newsbreeze.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.shuklansh.newsbreeze.domain.data.Article
import com.shuklansh.newsbreeze.domain.data.NewsResponse


@Entity(tableName = "NewsArticlesEntity")
data class NewsArticlesEntity(
//    @PrimaryKey(autoGenerate = true)
//    val id : Int? = null,
    @PrimaryKey
    val title : String = "title" ,
    val article: Article? = null
) {

    fun toArticle() : Article{
        return article!!
    }

}
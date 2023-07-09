package com.shuklansh.newsbreeze.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.shuklansh.newsbreeze.domain.data.Article
import java.time.format.DateTimeFormatter


@Entity(tableName = "ArticleEntity")
data class ArticleEntity(
//    @PrimaryKey(autoGenerate = true)
//    val id : Int? = null,
    @PrimaryKey
    val title : String = "title" ,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val url: String?,
    val urlToImage: String?,
)


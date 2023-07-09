package com.shuklansh.newsbreeze.domain.data

import com.shuklansh.newsbreeze.data.local.ArticleEntity

data class Article(
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    //val source: Source?,
    val title: String?,
    val url: String?,
    val urlToImage: String?,
    val bookmark : Boolean? = null,
) {

    fun toArticleEntity() : ArticleEntity {
        return ArticleEntity(
            title!!, author, content, description, publishedAt, url, urlToImage
        )
    }

}

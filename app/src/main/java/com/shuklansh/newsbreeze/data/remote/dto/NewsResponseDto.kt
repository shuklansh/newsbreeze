package com.shuklansh.newsbreeze.data.remote.dto

import com.shuklansh.newsbreeze.domain.data.NewsResponse

data class NewsResponseDto(
    val articles: List<ArticleDto>,
    val status: String,
    val totalResults: Int
) {
    fun toNewsResponse() : NewsResponse{
        return NewsResponse(
            articles = articles.map { it.toArticle() },
            //status = status,
            //totalResults = totalResults
        )
    }
}
package com.shuklansh.newsbreeze.presentation.utils

import com.shuklansh.newsbreeze.data.local.NewsArticlesEntity
import com.shuklansh.newsbreeze.domain.data.Article
import com.shuklansh.newsbreeze.domain.data.NewsResponse

data class bookmarked(
    val bookmarkBoolean : Boolean = false
)

data class NewsResultState(
    val newsArticles : List<Article> = emptyList(),
    val newsResponse : NewsResponse? = NewsResponse(emptyList()),
    val isLoading : Boolean = false
)

sealed class UiEvent{
    data class snackBarMessage(val message: String) : UiEvent()
}

data class LocalDbList(
    val articles: List<NewsArticlesEntity>? = emptyList()
)
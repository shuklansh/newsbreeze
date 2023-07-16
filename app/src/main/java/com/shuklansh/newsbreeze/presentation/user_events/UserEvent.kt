package com.shuklansh.newsbreeze.presentation.user_events

import com.shuklansh.newsbreeze.domain.data.Article
import java.util.Locale.Category

sealed interface UserEvent{
    data class BookmarkArticle(val article: Article) : UserEvent
    data class removeBookmarkArticle(val article: Article) : UserEvent
    data class getNewsByQuery(val query: String) : UserEvent
    data class getNewsByCategory(val category: String) : UserEvent
    object GetBookmarkArticlebyDate : UserEvent
    object GetBookmarkArticlebyOrderSave : UserEvent
}
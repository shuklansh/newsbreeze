package com.shuklansh.newsbreeze.presentation

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shuklansh.newsbreeze.core.util.Resource
import com.shuklansh.newsbreeze.data.local.NewsArticlesDatabase
import com.shuklansh.newsbreeze.data.local.NewsArticlesEntity
import com.shuklansh.newsbreeze.domain.data.Article
import com.shuklansh.newsbreeze.presentation.use_case.GetNewsForQueryRepository
import com.shuklansh.newsbreeze.presentation.use_case.NewsQueryUseCase
import com.shuklansh.newsbreeze.presentation.utils.LocalDbList
import com.shuklansh.newsbreeze.presentation.utils.NewsResultState
import com.shuklansh.newsbreeze.presentation.utils.UiEvent
import com.shuklansh.newsbreeze.presentation.utils.bookmarked
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    val getNewsUseCase : NewsQueryUseCase,
    val GetNewsForQueryRepository : GetNewsForQueryRepository,
    val db : NewsArticlesDatabase
) : ViewModel()  {

    private val _bookmarked = MutableStateFlow(bookmarked())
    val bookMarkedBool = _bookmarked.asStateFlow()

    private val _newsList = MutableStateFlow(NewsResultState())
    val newsList = _newsList.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var searchJob: Job? = null

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val _bookmarkedArticles = MutableStateFlow(LocalDbList())
    val bmlist = _bookmarkedArticles.asStateFlow()

    fun updateQuery(query : String) {
        _query.value = query
    }

//    fun updateBookmarkValue(value : Boolean){
//        _bookmarked.update {
//            it.copy(
//                bookmarkBoolean = value
//            )
//        }
//        Log.d("@#$$$",_bookmarked.value.bookmarkBoolean.toString())
//    }

    suspend fun addtheArticleToDb(article: Article){
        viewModelScope.launch {
            GetNewsForQueryRepository.repository.addtheArticleToDb(article)
        }
    }

    suspend fun removeFromDb(article: Article){
        viewModelScope.launch {
            GetNewsForQueryRepository.repository.removeFromDb(article)
        }
    }

    suspend fun getAllArticlesFromDb(){
        viewModelScope.launch {
            _bookmarkedArticles.update {
                it.copy(
                    articles = db.dao().getAllArticles()
                )
            }
        }
        Log.d("$%^","${db.dao().getAllArticles().size}")
    }

    suspend fun isarticleindb(article : Article) : Boolean{
        return db.dao().isarticleintheDB(article = article)
    }

    fun getNewsForQuery(query : String){
//        updateTranslationQuery(query)
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500L)
            getNewsUseCase(query).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        _newsList.value = _newsList.value.copy(
                            newsArticles = result.data?.articles ?: emptyList(),
                            newsResponse = result.data,
                            isLoading = false
                        )
                        _eventFlow.emit(UiEvent.snackBarMessage(result.message ?: "Unknown error "))
                    }
                    is Resource.Loading -> {
                        _newsList.value = _newsList.value.copy(
                            newsArticles = result.data?.articles ?: emptyList(),
                            newsResponse = result.data,
                            isLoading = true

                        )

                    }
                    is Resource.Success -> {
                        _newsList.value = _newsList.value.copy(
                            newsArticles = result.data?.articles ?: emptyList(),
                            newsResponse = result.data,
                            isLoading = false
                        )
                    }
                }
            }.launchIn(this)
        }
    }

    fun getNews(word : String){
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500L)
            GetNewsForQueryRepository(word).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        _newsList.value = _newsList.value.copy(
                            newsArticles = result.data?.articles ?: emptyList(),
                            newsResponse = result.data,
                            isLoading = false
                        )
                        _eventFlow.emit(UiEvent.snackBarMessage(result.message ?: "Unknown error "))
                    }
                    is Resource.Loading -> {
                        _newsList.value = _newsList.value.copy(
                            newsArticles = result.data?.articles ?: emptyList(),
                            newsResponse = result.data,
                            isLoading = true

                        )

                    }
                    is Resource.Success -> {
                        _newsList.value = _newsList.value.copy(
                            newsArticles = result.data?.articles ?: emptyList(),
                            newsResponse = result.data,
                            isLoading = false
                        )
                    }
                }
            }.launchIn(this)
        }
    }


}


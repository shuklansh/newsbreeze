package com.shuklansh.newsbreeze.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shuklansh.newsbreeze.core.util.Resource
import com.shuklansh.newsbreeze.data.local.NewsArticlesDatabase
import com.shuklansh.newsbreeze.domain.data.Article
import com.shuklansh.newsbreeze.presentation.use_case.GetNewsForQueryRepository
import com.shuklansh.newsbreeze.presentation.use_case.NewsQueryUseCase
import com.shuklansh.newsbreeze.presentation.user_events.UserEvent
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

//    private var _sort = MutableStateFlow(false)
//    val sort = _sort.asStateFlow()
//
//    private val _bookmarked = MutableStateFlow(bookmarked())
//    val bookMarkedBool = _bookmarked.asStateFlow()

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


    fun onEvent(event : UserEvent){
        when(event){
            is UserEvent.BookmarkArticle -> {
                viewModelScope.launch {
                    GetNewsForQueryRepository.repository.addtheArticleToDb(article = event.article)
                }
            }
            UserEvent.GetBookmarkArticlebyDate -> {
                viewModelScope.launch {
                    _bookmarkedArticles.update {

                        it.copy(
                            articles = db.dao().getAllArticlesByDate()
                        )

                    }
                }


            }
            UserEvent.GetBookmarkArticlebyOrderSave -> {
                viewModelScope.launch {
                _bookmarkedArticles.update {

                    it.copy(
                        articles = db.dao().getAllArticles()
                    )

                }
            }

            }
            is UserEvent.getNewsByCategory -> {
                getNews(word = event.category)

            }
            is UserEvent.getNewsByQuery -> {
                getNewsForQuery(query = event.query)

            }
            is UserEvent.removeBookmarkArticle -> {
                viewModelScope.launch {
                    GetNewsForQueryRepository.repository.removeFromDb(article = event.article)
                }

            }
        }

    }


//    fun updateBookmarkValue(value : Boolean){
//        _bookmarked.update {
//            it.copy(
//                bookmarkBoolean = value
//            )
//        }
//        Log.d("@#$$$",_bookmarked.value.bookmarkBoolean.toString())
//    }

//    suspend fun addtheArticleToDb(article: Article){
//        viewModelScope.launch {
//            GetNewsForQueryRepository.repository.addtheArticleToDb(article)
//        }
//    }
//
//    suspend fun removeFromDb(article: Article){
//        viewModelScope.launch {
//            GetNewsForQueryRepository.repository.removeFromDb(article)
//        }
//    }
//
//    suspend fun getAllArticlesFromDb(){
//        viewModelScope.launch {
//            _bookmarkedArticles.update {
//
//                    it.copy(
//                        articles = db.dao().getAllArticles()
//                    )
//
//            }
//        }
//        Log.d("$%^","${db.dao().getAllArticles().size}")
//    }
//
//    suspend fun getAllArticlesFromDbbydate(){
//        viewModelScope.launch {
//            _bookmarkedArticles.update {
//
//                    it.copy(
//                        articles = db.dao().getAllArticlesByDate()
//                    )
//
//            }
//        }
//        Log.d("$%^","${db.dao().getAllArticles().size}")
//    }

//    fun updateSortVal(){
//        if(sort.value==true){
//            _sort.value=false
//            Log.d("%%%",_sort.value.toString())
//        }else{
//            _sort.value=true
//            Log.d("%%%",_sort.value.toString())
//        }
//    }

    suspend fun isarticleindb(article : Article) : Boolean{
        return db.dao().isarticleintheDB(title = article.title!!)
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


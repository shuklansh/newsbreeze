package com.shuklansh.newsbreeze.data.repository

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.shuklansh.newsbreeze.core.util.Resource
import com.shuklansh.newsbreeze.data.local.NewsArticlesDatabase
import com.shuklansh.newsbreeze.data.local.NewsArticlesEntity
import com.shuklansh.newsbreeze.data.remote.NewsApi
import com.shuklansh.newsbreeze.domain.data.Article
import com.shuklansh.newsbreeze.domain.data.NewsResponse
import com.shuklansh.newsbreeze.domain.repository.GetNewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class GetNewsRepositoryImpl(
    private val api : NewsApi,
    private val db : NewsArticlesDatabase
) : GetNewsRepository {

    override fun getNewsWithQuery(query: String): Flow<Resource<NewsResponse>> = flow{

        emit(Resource.Loading())

        try {

            val newsResponseFromApi = api.getNewsForTopic(query)
            emit(Resource.Success(data = newsResponseFromApi.toNewsResponse()))

        } catch (e : HttpException){

            emit(Resource.Error(message = "Could not find result for query $query"))
            //data = NewsResponse(articles = db.dao.getAllArticles())

        } catch (e : IOException){

            emit(Resource.Error(message = "Check your network connection"))
            //data = NewsResponse(articles = db.dao.getAllArticles())

        }

    }

    override fun getNewsForQuery(query: String): Flow<Resource<NewsResponse>> = flow{

        emit(Resource.Loading())

        try {

            val newsResponseFromApi = api.getNewsForQuery(query)
            emit(Resource.Success(data = newsResponseFromApi.toNewsResponse()))

        } catch (e : HttpException){

            emit(Resource.Error(message = "Could not find result for query $query"))
            //data = NewsResponse(articles = db.dao.getAllArticles())

        } catch (e : IOException){

            emit(Resource.Error(message = "Check your network connection"))
            //data = NewsResponse(articles = db.dao.getAllArticles())

        }

    }

    override suspend fun addtheArticleToDb(article: Article){

            db.dao().bookmarkArticle(article = NewsArticlesEntity(article = article, title = article.title ?: "title"))
            Log.d("$%^","${article.title} added to db")

    }

    override suspend fun removeFromDb(article: Article){

            db.dao().deleteArticle(article = NewsArticlesEntity(article = article, title = article.title ?: "title"))
            Log.d("$%^","${article.title} removed from db")

    }


}
package com.shuklansh.newsbreeze.data.repository

import com.shuklansh.newsbreeze.core.util.Resource
import com.shuklansh.newsbreeze.data.local.NewsArticlesDatabase
import com.shuklansh.newsbreeze.data.remote.NewsApi
import com.shuklansh.newsbreeze.domain.data.NewsResponse
import com.shuklansh.newsbreeze.domain.repository.GetNewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class GetNewsRepositoryImpl(
    private val api : NewsApi,
    private val db : NewsArticlesDatabase
) : GetNewsRepository {

    override fun getNewsWithQuery(query: String): Flow<Resource<NewsResponse>> = flow{

        emit(Resource.Loading())

        try {

            val newsResponseFromApi = api.getNewsFromQuery(query)
            emit(Resource.Success(data = newsResponseFromApi.toNewsResponse()))

        } catch (e : HttpException){

            emit(Resource.Error(message = "Could not find result for query $query"))
            //data = NewsResponse(articles = db.dao.getAllArticles())

        } catch (e : IOException){

            emit(Resource.Error(message = "Check your network connection"))
            //data = NewsResponse(articles = db.dao.getAllArticles())

        }

    }


}
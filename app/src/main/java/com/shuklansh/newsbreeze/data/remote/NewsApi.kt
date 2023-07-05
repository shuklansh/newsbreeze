package com.shuklansh.newsbreeze.data.remote

import com.shuklansh.newsbreeze.BuildConfig
import com.shuklansh.newsbreeze.data.remote.dto.NewsResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun getNewsForTopic(
        @Query("query") query: String,
        @Query("country") country: String = "in",
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY,
    ) : NewsResponseDto

    @GET("v2/top-headlines")
    suspend fun getNewsForQuery(
        @Query("q") query: String,
//        @Query("country") country: String = "in",
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY,
    ) : NewsResponseDto


    companion object{
        val BASE_URL = "https://newsapi.org/"
    }

}
package com.shuklansh.newsbreeze.data.remote

import com.shuklansh.newsbreeze.BuildConfig
import com.shuklansh.newsbreeze.data.remote.dto.NewsResponseDto
import com.shuklansh.newsbreeze.domain.data.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun getNewsFromQuery(
        @Query("category") query: String,
        @Query("country") country: String = "in",
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY,
    ) : NewsResponseDto

    companion object{
        val BASE_URL = "https://newsapi.org/"
    }

}
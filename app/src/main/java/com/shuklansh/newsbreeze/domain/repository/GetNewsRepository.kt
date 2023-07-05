package com.shuklansh.newsbreeze.domain.repository

import com.shuklansh.newsbreeze.core.util.Resource
import com.shuklansh.newsbreeze.domain.data.NewsResponse
import kotlinx.coroutines.flow.Flow

interface GetNewsRepository {

    fun getNewsWithQuery(query : String) : Flow<Resource<NewsResponse>>

    fun getNewsForQuery(query: String) : Flow<Resource<NewsResponse>>

}
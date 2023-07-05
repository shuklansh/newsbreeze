package com.shuklansh.newsbreeze.presentation.use_case

import com.shuklansh.newsbreeze.core.util.Resource
import com.shuklansh.newsbreeze.domain.data.NewsResponse
import com.shuklansh.newsbreeze.domain.repository.GetNewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetNewsForQueryRepository(
    val repository: GetNewsRepository,
) {

    operator fun invoke(query : String) : Flow<Resource<NewsResponse>> {
        if(query.isBlank()){
            return flow {  }
        }
        return repository.getNewsWithQuery(query)
    }

}
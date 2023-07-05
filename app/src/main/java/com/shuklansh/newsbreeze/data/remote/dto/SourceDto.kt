package com.shuklansh.newsbreeze.data.remote.dto

import com.shuklansh.newsbreeze.domain.data.Source

data class SourceDto(
    val id: Any,
    val name: String
) {

    fun toSource() : Source{
        return Source(
            id  = id,
            name = name
        )
    }

}
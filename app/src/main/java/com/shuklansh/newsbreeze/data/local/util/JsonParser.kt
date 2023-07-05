package com.shuklansh.dictionaryapp.feature_dictionary.data.util

import java.lang.reflect.Type

interface JsonParser {
    // implementation of interface used to covert the data of generic data type (like meaning) to simple json so it can be stored in entity

    fun <T> fromJson(json : String, type : Type) : T?

    fun <T> toJson(obj : T, type:Type) : String?

}
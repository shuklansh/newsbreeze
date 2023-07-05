package com.shuklansh.newsbreeze.data.local

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.reflect.TypeToken
import com.shuklansh.dictionaryapp.feature_dictionary.data.util.JsonParser
import com.shuklansh.newsbreeze.domain.data.Article

// here we specify the type of converter (gson/moshi etc)
@ProvidedTypeConverter //this annotation used because by default typeconverters cant have constructor args
class Converters(
    private val jsonParser: JsonParser
) {

    // function to take a string and convert it to list of meanings
    @TypeConverter
    fun fromArticleJson(json : String) : Article?{
        return jsonParser.fromJson<Article>(
            json = json,
            type = object : TypeToken<Article?>(){}.type) ?: null
    }

    // function to take a list of meanings and convert it to string as json to be stored in entity
    @TypeConverter
    fun toArticleJson(newsArticles : Article) : String{
        return jsonParser.toJson(
            obj = newsArticles,
            type = object : TypeToken<Article>(){}.type
        ) ?: "null"
    }

}
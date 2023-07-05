package com.shuklansh.newsbreeze.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(
    entities = [NewsArticlesEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class NewsArticlesDatabase : RoomDatabase() {

    abstract fun dao() : NewsArticlesDao

}
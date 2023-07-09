package com.shuklansh.newsbreeze.di

import android.app.Application
import androidx.room.Room
import com.google.gson.Gson
import com.shuklansh.dictionaryapp.feature_dictionary.data.util.GsonParser
import com.shuklansh.newsbreeze.data.local.Converters
import com.shuklansh.newsbreeze.data.local.NewsArticlesDatabase
import com.shuklansh.newsbreeze.data.remote.NewsApi
import com.shuklansh.newsbreeze.data.repository.GetNewsRepositoryImpl
import com.shuklansh.newsbreeze.domain.repository.GetNewsRepository
import com.shuklansh.newsbreeze.presentation.use_case.GetNewsForQueryRepository
import com.shuklansh.newsbreeze.presentation.use_case.NewsQueryUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NewsBreezeModule {

    @Singleton
    @Provides
    fun getUseCase(
        repository: GetNewsRepository
    ) : NewsQueryUseCase {
        return NewsQueryUseCase(
            repository = repository
        )
    }

    @Singleton
    @Provides
    fun getUseCaseQuery(
        repository: GetNewsRepository
    ) : GetNewsForQueryRepository {
        return GetNewsForQueryRepository(
            repository = repository
        )
    }

    @Singleton
    @Provides
    fun getNewsRepository(
        api : NewsApi,
        db : NewsArticlesDatabase
    ) : GetNewsRepository {
        return GetNewsRepositoryImpl(
            api = api,
            db = db
        )
    }


    @Singleton
    @Provides
    fun provideRetrofitApi(): NewsApi {
        return Retrofit.Builder()
            .baseUrl(NewsApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApi::class.java)
    }


//    @Singleton
//    @Provides
//    fun provideDatabase(app : Application) : NewsArticlesDatabase{
//        return Room.databaseBuilder(
//            app,
//            klass = NewsArticlesDatabase::class.java,
//            name = "NewsArticlesDb"
//        ).addTypeConverter(Converters(GsonParser(Gson()))).fallbackToDestructiveMigration().build()
//    }
//
    @Singleton
    @Provides
    fun provideDatabase(app : Application) : NewsArticlesDatabase{
        return Room.databaseBuilder(
            app,
            klass = NewsArticlesDatabase::class.java,
            name = "NewsArticlesDb"
        ).fallbackToDestructiveMigration().build()
    }


}
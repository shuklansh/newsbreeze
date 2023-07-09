//package com.shuklansh.newsbreeze
//
//import androidx.room.Room
//import androidx.test.core.app.ApplicationProvider
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import androidx.test.filters.SmallTest
//import com.google.gson.Gson
//import com.shuklansh.dictionaryapp.feature_dictionary.data.util.GsonParser
//import com.shuklansh.newsbreeze.data.local.Converters
//import com.shuklansh.newsbreeze.data.local.NewsArticlesDao
//import com.shuklansh.newsbreeze.data.local.NewsArticlesDatabase
//import com.shuklansh.newsbreeze.data.local.ArticleEntity
//import com.shuklansh.newsbreeze.domain.data.Article
//import kotlinx.coroutines.runBlocking
//import org.junit.*
//import org.junit.runner.RunWith
//
//@RunWith(AndroidJUnit4::class)
//@SmallTest
//class AppDatabaseTest{
//
//
//        private lateinit var newsArticlesDatabase : NewsArticlesDatabase
//        private lateinit var newsArticlesDao : NewsArticlesDao
//
//        @Before
//        fun setUp(){
//            newsArticlesDatabase = Room.inMemoryDatabaseBuilder(
//                ApplicationProvider.getApplicationContext(),
//                NewsArticlesDatabase::class.java
//            ).allowMainThreadQueries().addTypeConverter(Converters(GsonParser(Gson()))).build()
//            newsArticlesDao = newsArticlesDatabase.dao()
//
//
//        }
//
//        @After
//        fun tearDown(){
//            newsArticlesDatabase.close()
//
//        }
//
//
//        @Test
//        fun upsertArticle_expected_Single_NewsTitle() = runBlocking{
//            val testArticle = Article("","","","","TestArticle","","",false)
//            val testNewsArticlesEntity = ArticleEntity(testArticle.title!!,testArticle)
//            newsArticlesDao.bookmarkArticle(testNewsArticlesEntity)
//
//            var result = newsArticlesDao.getAllArticles()[0].title
//            Assert.assertEquals("TestArticle",result)
//
//    }
//
//    @Test
//    fun upsertArticle_expected_Single_NewsEntity() = runBlocking{
//        val testArticle = Article("","","","","TestArticle","","",false)
//        val testNewsArticlesEntity = ArticleEntity(testArticle.title!!,testArticle)
//        newsArticlesDao.bookmarkArticle(testNewsArticlesEntity)
//
//        var result = newsArticlesDao.getAllArticles()
//        Assert.assertEquals(1,result.size)
//
//    }
//
//    @Test
//    fun deleteArticle_expected_size_Zero() = runBlocking{
//        val testArticle = Article("","","","","TestArticle","","",false)
//        val testNewsArticlesEntity = ArticleEntity(testArticle.title!!,testArticle)
//        newsArticlesDao.bookmarkArticle(testNewsArticlesEntity)
//        newsArticlesDao.deleteArticle(testNewsArticlesEntity)
//        var result = newsArticlesDao.getAllArticles()
//
//        Assert.assertEquals(0,result.size)
//
//    }
//
//
//}
//

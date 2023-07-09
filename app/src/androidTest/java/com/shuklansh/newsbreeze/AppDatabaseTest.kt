package com.shuklansh.newsbreeze

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.gson.Gson
import com.shuklansh.dictionaryapp.feature_dictionary.data.util.GsonParser
import com.shuklansh.newsbreeze.data.local.Converters
import com.shuklansh.newsbreeze.data.local.NewsArticlesDao
import com.shuklansh.newsbreeze.data.local.NewsArticlesDatabase
import com.shuklansh.newsbreeze.data.local.ArticleEntity
import com.shuklansh.newsbreeze.domain.data.Article
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class AppDatabaseTest{


        private lateinit var newsArticlesDatabase : NewsArticlesDatabase
        private lateinit var newsArticlesDao : NewsArticlesDao

        @Before
        fun setUp(){
            newsArticlesDatabase = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                NewsArticlesDatabase::class.java
            ).allowMainThreadQueries().build()
            newsArticlesDao = newsArticlesDatabase.dao()


        }

        @After
        fun tearDown(){
            newsArticlesDatabase.close()

        }


        @Test
        fun upsertArticle_expected_Single_NewsTitle() = runBlocking{
            val testArticle = Article("","","","","TestArticle","","",false)
            val testNewsArticlesEntity = testArticle.toArticleEntity()
            newsArticlesDao.bookmarkArticle(testNewsArticlesEntity)

            var result = newsArticlesDao.getAllArticles()[0].title
            Assert.assertEquals("TestArticle",result)

    }

    @Test
    fun upsertArticle_expected_Single_NewsEntity() = runBlocking{
        val testArticle = Article("","","","","TestArticle","","",false)
        val testNewsArticlesEntity = testArticle.toArticleEntity()
        newsArticlesDao.bookmarkArticle(testNewsArticlesEntity)

        var result = newsArticlesDao.getAllArticles()
        Assert.assertEquals(1,result.size)

    }

    @Test
    fun deleteArticle_expected_size_Zero() = runBlocking{
        val testArticle = Article("","","","","TestArticle","","",false)
        val testNewsArticlesEntity = testArticle.toArticleEntity()
        newsArticlesDao.bookmarkArticle(testNewsArticlesEntity)
        newsArticlesDao.deleteArticle(testNewsArticlesEntity)
        var result = newsArticlesDao.getAllArticles()
        Assert.assertEquals(0,result.size)
    }

    @Test
    fun sortArticleby_date_expected_size_Zero() = runBlocking{
        val testArticle1 = Article("","","","02-03-2023","TestArticle1","","",false)
        val testArticle2 = Article("","","","03-03-2023","TestArticle2","","",false)
        val testNewsArticlesEntity1 = testArticle1.toArticleEntity()
        val testNewsArticlesEntity2 = testArticle2.toArticleEntity()
        newsArticlesDao.bookmarkArticle(testNewsArticlesEntity1)
        newsArticlesDao.bookmarkArticle(testNewsArticlesEntity2)
        var testArticlesList = newsArticlesDao.getAllArticlesByDate()
        var stringdatesort = testArticlesList[0].publishedAt + "," + testArticlesList[1].publishedAt
        Assert.assertEquals("03-03-2023,02-03-2023",stringdatesort)
    }


}


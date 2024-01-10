package com.test.newsappproject

import com.test.newsappproject.dataModel.NewsRss
import retrofit2.Call
import retrofit2.http.GET

interface NewService {
    @GET("rss")
    fun mainFeed(): Call<NewsRss>

    @GET("rss/politics/")
    fun politicsNews(): Call<NewsRss>

    @GET("rss/economy/")
    fun economyNews(): Call<NewsRss>

    @GET("rss/society/")
    fun societyNews(): Call<NewsRss>

    @GET("rss/international/")
    fun internationalNews(): Call<NewsRss>

    @GET("rss/culture/")
    fun cultureNews(): Call<NewsRss>

    @GET("rss/enter/")
    fun enterNews(): Call<NewsRss>

    @GET("rss/sports/")
    fun sportsNews(): Call<NewsRss>

    @GET("rss/health/")
    fun healthNews(): Call<NewsRss>
}
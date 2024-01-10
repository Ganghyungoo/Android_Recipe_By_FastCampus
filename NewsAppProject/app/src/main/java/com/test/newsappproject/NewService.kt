package com.test.newsappproject

import com.test.newsappproject.dataModel.NewsRss
import retrofit2.Call
import retrofit2.http.GET

interface NewService {
    @GET("rss")
    fun mainFeed(): Call<NewsRss>
}
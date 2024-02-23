package com.test.searchmediaproject.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitManager {
    private val logging = HttpLoggingInterceptor().apply {
        // 요청과 응답의 본문 내용까지 로그에 포함
        level = HttpLoggingInterceptor.Level.BODY
    }

    //OKHttp Logging Interceptor
    private val okHttpClientInterceptor = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(5,TimeUnit.SECONDS)
        .readTimeout(5,TimeUnit.SECONDS)
        .writeTimeout(5,TimeUnit.SECONDS)
        .build()
    private val gson = GsonBuilder()
        .setLenient()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        .create()
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://dapi.kakao.com/v2/search/")
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .client(okHttpClient)
        .client(okHttpClientInterceptor)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val searchService: SearchService by lazy { retrofit.create(SearchService::class.java) }
}
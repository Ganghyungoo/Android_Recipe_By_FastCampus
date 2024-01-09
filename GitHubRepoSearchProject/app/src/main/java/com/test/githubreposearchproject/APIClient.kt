package com.test.githubreposearchproject

import okhttp3.OkHttp
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APIClient {
    private const val BASE_URL = "https://api.github.com/"

    private val logging = HttpLoggingInterceptor().apply {
        // 요청과 응답의 본문 내용까지 로그에 포함
        level = HttpLoggingInterceptor.Level.BODY
    }

    //해더를 일일히 저장하기 힘들기에 클라이언트에 삽입
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor {
            val request = it.request().newBuilder()
                .addHeader("Authorization", "Bearer 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")
                .build()
            it.proceed(request)
        }
        .build()

    //OKHttp Logging Interceptor
    private val okHttpClientInterceptor = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()


    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .client(okHttpClientInterceptor)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
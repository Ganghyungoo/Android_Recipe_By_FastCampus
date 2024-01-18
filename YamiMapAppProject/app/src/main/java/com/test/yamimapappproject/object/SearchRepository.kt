package com.test.yamimapappproject.`object`

import android.content.res.Resources
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.test.yamimapappproject.MyApplication
import com.test.yamimapappproject.R
import com.test.yamimapappproject.SearchService
import com.test.yamimapappproject.dataclass.SearchResult
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object SearchRepository {

    private val logging = HttpLoggingInterceptor().apply {
        // 요청과 응답의 본문 내용까지 로그에 포함
        level = HttpLoggingInterceptor.Level.BODY
    }

    //레트로핏 인터셉터를 okHttpClient에 연결
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AppInterceptor())
        .addInterceptor(logging)
        .build()

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://openapi.naver.com/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(okHttpClient)
        .build()

    private val service = retrofit.create(SearchService::class.java)

    fun getYamiRestaurantRetrofitService(query:String): Call<SearchResult>{
        return service.yamiRestaurant(query = "$query 맛집", display = 5)
    }

    //헤더 설정을 위한 인터셉터 구현
    class AppInterceptor:Interceptor{
        override fun intercept(chain: Interceptor.Chain): Response {
            val clientId = MyApplication.applicationContext.getString(R.string.naver_search_client_id)
            val clientSecret = MyApplication.applicationContext.getString(R.string.naver_search_client_secret)
            val newRequest = chain.request().newBuilder()
                .addHeader("X-Naver-Client-Id",clientId)
                .addHeader("X-Naver-Client-Secret",clientSecret)
                .build()

           return chain.proceed(newRequest)
        }

    }

}
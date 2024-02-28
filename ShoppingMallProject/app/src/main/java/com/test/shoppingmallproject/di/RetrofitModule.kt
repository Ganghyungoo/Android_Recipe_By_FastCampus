package com.test.shoppingmallproject.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.test.shoppingmallproject.model.ListItem
import com.test.shoppingmallproject.remote.ListItemDeserializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    fun providesConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create(
            GsonBuilder()
                .registerTypeAdapter(ListItem::class.java, ListItemDeserializer())
                .create()
        )
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient.Builder{
        return OkHttpClient.Builder().apply {
            connectTimeout(5, TimeUnit.SECONDS)
            readTimeout(5,TimeUnit.SECONDS)
            writeTimeout(5,TimeUnit.SECONDS)
        }
    }

    @Provides
    @Singleton
    fun providesRetrofit(
        client: OkHttpClient.Builder,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.31.16:3030/api/v1/fastcampus/")
            .addConverterFactory(gsonConverterFactory)
            .client(client.build())
            .build()
    }
}
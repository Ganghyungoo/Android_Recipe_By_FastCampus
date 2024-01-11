package com.test.newsappproject.`object`

import com.test.newsappproject.network.NewService
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import retrofit2.Retrofit

object APIClient{
    private const val BASE_URL = "https://www.mbn.co.kr/"

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        //xml 형식 변환
        .addConverterFactory(
            TikXmlConverterFactory.create(
                TikXml.Builder()
                    .exceptionOnUnreadXml(false)
                    .build()
            )
        ).build()

    val newService = retrofit.create(NewService::class.java)
}
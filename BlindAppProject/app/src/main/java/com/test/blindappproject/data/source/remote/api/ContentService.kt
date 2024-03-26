package com.test.blindappproject.data.source.remote.api

import com.test.blindappproject.data.model.dto.ContentDto
import com.test.blindappproject.data.model.dto.ContentResponse
import com.test.blindappproject.data.model.dto.ListResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ContentService {
    @GET("list")
    suspend fun getList(): ListResponse

    @GET("save")
    suspend fun saveItem(@Body contentDto: ContentDto): ContentResponse

    @POST("update")
    suspend fun updateItem(@Body contentDto: ContentDto): ContentResponse

    @DELETE
    suspend fun deleteItem(@Path("id") id: Int): ContentResponse
}
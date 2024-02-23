package com.test.searchmediaproject.network


import com.test.searchmediaproject.model.ImageListResponse
import com.test.searchmediaproject.model.VideoListResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface SearchService {
    @Headers("Authorization: KakaoAK b051917fd6d9513cd790631c852855c1")
    @GET("image")
    fun searchImage(@Query("query") query: String): Observable<ImageListResponse>

    @Headers("Authorization: KakaoAK b051917fd6d9513cd790631c852855c1")
    @GET("vclip")
    fun searchVideo(@Query("query") query: String): Observable<VideoListResponse>
}
package com.test.yamimapappproject

import com.test.yamimapappproject.dataclass.SearchResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {
    @GET("v1/search/local.json")
    fun yamiRestaurant(
        @Query("query") query: String,
        @Query("display") display: Int = 5,
    ): Call<SearchResult>
}
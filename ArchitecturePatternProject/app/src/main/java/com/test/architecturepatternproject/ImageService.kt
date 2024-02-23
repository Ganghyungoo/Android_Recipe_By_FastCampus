package com.test.architecturepatternproject

import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface ImageService {

    @Headers("Authorization: Client-ID Cbcil-_q7mhMDIXTIFz5agv3Zk92SVhQd7yvt2_yZzM")
    @GET("/photos/random")
    fun getRandomImage():Call<ImageResponse>

    @Headers("Authorization: Client-ID Cbcil-_q7mhMDIXTIFz5agv3Zk92SVhQd7yvt2_yZzM")
    @GET("/photos/random")
    fun getRandomImageRx(): Single<ImageResponse>
}
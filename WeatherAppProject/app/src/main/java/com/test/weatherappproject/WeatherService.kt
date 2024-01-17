package com.test.weatherappproject

import com.test.weatherappproject.dataclass.ResponseData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("1360000/VilageFcstInfoService_2.0/getVilageFcst?&pageNo=1&numOfRows=1000&dataType=JSON")
    fun getVillageForecast(
        @Query("serviceKey") serviceKey:String,
        @Query("base_date") baseDate:String,
        @Query("base_time") baseTime:String,
        @Query("nx") nx:Int,
        @Query("ny") ny:Int
    ): Call<ResponseData>
}
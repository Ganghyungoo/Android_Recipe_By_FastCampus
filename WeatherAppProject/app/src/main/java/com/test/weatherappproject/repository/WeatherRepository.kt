package com.test.weatherappproject.repository

import android.util.Log
import com.test.weatherappproject.GeoPointConverter
import com.test.weatherappproject.NetworkApi
import com.test.weatherappproject.R
import com.test.weatherappproject.databinding.ItemForecastBinding
import com.test.weatherappproject.dataclass.BaseDateTime
import com.test.weatherappproject.dataclass.Category
import com.test.weatherappproject.dataclass.Forecast
import com.test.weatherappproject.dataclass.Item
import com.test.weatherappproject.dataclass.ResponseData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.NullPointerException

object WeatherRepository {
    fun getVillageForecast(
        lat: Double,
        lon: Double,
        successCallback: (List<Forecast>) -> Unit,
        failCallback: (t: Throwable) -> Unit,
    ) {
        //오늘의 날짜와 시간을 데이터 포펫에 맞게 얻어서 통신하기 위한 작업
        val baseDateTime = BaseDateTime()
        baseDateTime.setBaseDateTime()

        //좌표를 위도 경도로 변환해주기 위한 클래스 사용
        val converter = GeoPointConverter()
        val point = converter.convert(lat = lat, lon = lon)

        NetworkApi.getRetrofitService().getVillageForecast(

//            serviceKey = R.string.weather_service_key.toString(),
            serviceKey = "hojdaAj28uKSvkkT5O01VLlmsMbVDxwWfk5norTQMAdtVK6+18evQogPO5ix63vdVPoPG6hGVUGv2iZ3nKzJvA==",
            baseDate = baseDateTime.baseDate,
            baseTime = baseDateTime.baseTime,
            nx = point.nx,
            ny = point.ny
        ).enqueue(object : Callback<ResponseData> {
            override fun onResponse(
                call: Call<ResponseData>,
                response: Response<ResponseData>,
            ) {
                val forecastDateTimeMap = mutableMapOf<String, Forecast>()
                response.body()?.response?.body?.items?.item?.forEach {
                    if (forecastDateTimeMap["${it.fcstDate}/${it.fcstTime}"] == null) {
                        forecastDateTimeMap["${it.fcstDate}/${it.fcstTime}"] =
                            Forecast(forecastDate = it.fcstDate, forecastTime = it.fcstTime)
                    }

                    forecastDateTimeMap["${it.fcstDate}/${it.fcstTime}"]?.apply {
                        when (it.category) {
                            Category.POP -> {
                                precipitation = it.fcstValue.toInt()
                            }

                            Category.PTY -> {
                                precipitationType = transFormRainType(it)
                            }

                            Category.SKY -> {
                                sky = transFormSkyType(it)
                            }

                            Category.TMP -> {
                                temperature = it.fcstValue.toDouble()
                            }

                            else -> {}
                        }
                    }
                }

                //정리한 데이터에서 가장 최근(현재)날씨를 가져오고 날짜별로 정리한 리스트로 변환
                val list = forecastDateTimeMap.values.toMutableList()
                list.sortWith { f1, f2 ->
                    val f1DateTime = "${f1.forecastDate}${f1.forecastTime}"
                    val f2DateTime = "${f2.forecastDate}${f2.forecastTime}"

                    return@sortWith f1DateTime.compareTo(f2DateTime)
                }
                //ui설정
                if (list.isEmpty()){
                    failCallback(NullPointerException())
                }else{
                    successCallback(list)
                }



            }

            override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                failCallback(t)

            }

        })

    }

    private fun transFormRainType(forecast: Item): String {
        return when (forecast.fcstValue.toInt()) {
            0 -> "없음"
            1 -> "비/눈"
            2 -> "눈"
            3 -> "소나기"
            else -> ""
        }
    }

    private fun transFormSkyType(forecast: Item): String {
        return when (forecast.fcstValue.toInt()) {
            1 -> "맑음"
            3 -> "구름 많음"
            4 -> "흐림"
            else -> ""
        }
    }
}
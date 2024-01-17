package com.test.weatherappproject

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.test.weatherappproject.databinding.ActivityMainBinding
import com.test.weatherappproject.dataclass.BaseDateTime
import com.test.weatherappproject.dataclass.Category
import com.test.weatherappproject.dataclass.Forecast
import com.test.weatherappproject.dataclass.Item
import com.test.weatherappproject.dataclass.ResponseData
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding

    val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(android.Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
                updateLocation()
            }

            else -> {
                Toast.makeText(this, "위치 권한이 필요합니다", Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package",packageName,null)
                }
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        locationPermissionRequest.launch(arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION))





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

    private fun updateLocation(){
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionRequest.launch(arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION))
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener {
            Log.d("myLocation", "위도: ${it.latitude} 경도: ${it.longitude}")
            //오늘의 날짜와 시간을 데이터 포펫에 맞게 얻어서 통신하기 위한 작업
            val baseDateTime = BaseDateTime()
            baseDateTime.setBaseDateTime()

            //좌표를 위도 경도로 변환해주기 위한 클래스 사용
            val converter = GeoPointConverter()
            val point = converter.convert(lat = it.latitude, lon = it.longitude)

            NetworkApi.getRetrofitService().getVillageForecast(
                serviceKey = getString(R.string.weather_service_key),
                baseDate = baseDateTime.baseDate,
                baseTime = baseDateTime.baseTime,
                nx = point.nx,
                ny = point.ny
            ).enqueue(object : Callback<ResponseData> {
                override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
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
                    Log.d("netWorkFunction", "${forecastDateTimeMap}")
                }

                override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                    Log.e("netWorkFunction", "${t.message}")
                }

            })
        }.addOnFailureListener {
            Log.d("myLocation", "long: ${it.message}")
        }
    }

}
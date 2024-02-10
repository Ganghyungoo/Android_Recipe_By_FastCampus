package com.test.weatherappproject

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
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
import com.test.weatherappproject.databinding.ItemForecastBinding
import com.test.weatherappproject.dataclass.BaseDateTime
import com.test.weatherappproject.dataclass.Category
import com.test.weatherappproject.dataclass.Forecast
import com.test.weatherappproject.dataclass.Item
import com.test.weatherappproject.dataclass.ResponseData
import com.test.weatherappproject.repository.WeatherRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding

    private val locationPermissionRequest = registerForActivityResult(
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
                    data = Uri.fromParts("package", packageName, null)
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


    private fun updateLocation() {
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
            //내가 있는 현재 위치의 동네이름을 알아내기위한 작업
            Thread {
                try {
                    val addressList = Geocoder(this, Locale.KOREA).getFromLocation(
                        it.latitude,
                        it.longitude,
                        1
                    )
                    runOnUiThread {
                        Log.d("testt", addressList?.get(0)?.thoroughfare.orEmpty())
                        activityMainBinding.locationTextView.text =
                            addressList?.get(0)?.thoroughfare.orEmpty()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }.start()

            WeatherRepository.getVillageForecast(lat = it.latitude, lon = it.longitude, { list->
                val currentForecast = list.first()
                activityMainBinding.temperatureTexView.text =
                    getString(R.string.temperature_text, currentForecast.temperature)
                activityMainBinding.skyTextView.text = currentForecast.weather
                activityMainBinding.precipitationTextView.text =
                    getString(R.string.precipitation_text, currentForecast.precipitation)

                list.forEachIndexed { index, forecast ->
                    if (index == 0) return@forEachIndexed

                    val itemView = ItemForecastBinding.inflate(layoutInflater)
                    itemView.timeTextView.text = forecast.forecastTime
                    itemView.weatherTextView.text = forecast.weather
                    itemView.temperatureTexView.text =
                        getString(R.string.temperature_text, forecast.temperature)

                    activityMainBinding.childForecastLayout.addView(itemView.root)
                }
                Log.d("netWorkFunction", "${list}")
            }, {
                it.printStackTrace()
            },this)


            Log.d("myLocation", "위도: ${it.latitude} 경도: ${it.longitude}")
        }.addOnFailureListener {
            Log.d("myLocation", "long: ${it.message}")
        }
    }

}
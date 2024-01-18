package com.test.yamimapappproject

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.test.yamimapappproject.databinding.ActivityMainBinding
import com.test.yamimapappproject.dataclass.SearchResult
import com.test.yamimapappproject.`object`.SearchRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var naverMap: NaverMap
    private var isNaverMapInit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        activityMainBinding.mapView.onCreate(savedInstanceState)

        activityMainBinding.mapView.getMapAsync(this)

        SearchRepository.getYamiRestaurantRetrofitService("석남동").enqueue(object: Callback<SearchResult>{
            override fun onResponse(call: Call<SearchResult>, response: Response<SearchResult>) {


                Log.d("textt","${response.body()}")
            }

            override fun onFailure(call: Call<SearchResult>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

        if (isNaverMapInit){

        }
    }

    override fun onStart() {
        super.onStart()
        activityMainBinding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        activityMainBinding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        activityMainBinding.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        activityMainBinding.mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        activityMainBinding.mapView.onDestroy()

    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        activityMainBinding.mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        activityMainBinding.mapView.onLowMemory()
    }

    override fun onMapReady(p0: NaverMap) {
        naverMap = p0
        isNaverMapInit = true

        val cameraUpdate = CameraUpdate.scrollTo(LatLng(37.5666102, 126.9783881))
            .animate(CameraAnimation.Easing)
        naverMap.moveCamera(cameraUpdate)
    }

}
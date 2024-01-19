package com.test.yamimapappproject

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.Tm128
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.test.yamimapappproject.adapter.RestaurantListAdapter
import com.test.yamimapappproject.databinding.ActivityMainBinding
import com.test.yamimapappproject.dataclass.SearchResult
import com.test.yamimapappproject.`object`.SearchRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var naverMap: NaverMap
    private var restaurantListAdapter = RestaurantListAdapter{

        //아이템 클릭 시 카메라 이동
        val cameraUpdate = CameraUpdate.scrollTo(it)
            .animate(CameraAnimation.Easing)
        naverMap.moveCamera(cameraUpdate)

        //바텀 시트 내리기작업
        downToBottomSheet()

    }
    private var isNaverMapInit = false
    private var markerList = mutableListOf<Marker>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        activityMainBinding.mapView.onCreate(savedInstanceState)

        activityMainBinding.mapView.getMapAsync(this)

        activityMainBinding.bottomSheetInclude.searchResultRecyclerView.apply {
                adapter =  restaurantListAdapter
                layoutManager = LinearLayoutManager(this@MainActivity)
        }

        activityMainBinding.searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return if (query?.isNotEmpty() == true) {
                    SearchRepository.getYamiRestaurantRetrofitService(query)
                        .enqueue(object : Callback<SearchResult> {
                            override fun onResponse(
                                call: Call<SearchResult>,
                                response: Response<SearchResult>,
                            ) {
                                val searchItemList = response.body()?.items.orEmpty()
                                if (searchItemList.isEmpty()) {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "검색 결과가 존재하지 않습니다",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return
                                } else if (!isNaverMapInit) {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "오류가 발생 중입니다",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return
                                }
                                //TODO 지도의 모든 마커 삭제작업
                                if (markerList.isNotEmpty()){
                                    markerList.forEach {
                                        it.map = null
                                    }
                                }
                                //리사이클러뷰 업데이트
                                restaurantListAdapter.updateList(searchItemList)

                                val markers = searchItemList.map {
                                    Marker().apply {
                                        val xy = LatLng(
                                            it.mapy.toDouble() / 10000000,
                                            it.mapx.toDouble() / 10000000
                                        )
                                        position = xy
                                        captionText = it.title
                                        map = naverMap
                                    }
                                }
                                markerList.clear()
                                markerList.addAll(markers)

                                Log.d("testt","${markerList.first().captionText}")
                                val cameraUpdate = CameraUpdate.scrollTo(markerList.first().position)
                                    .animate(CameraAnimation.Easing)
                                naverMap.moveCamera(cameraUpdate)
                                //바텀 시트 내리기
                                downToBottomSheet()
                            }

                            override fun onFailure(call: Call<SearchResult>, t: Throwable) {
                                TODO("Not yet implemented")
                            }

                        })
                    false
                } else {
                    true
                }
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })



        if (isNaverMapInit) {

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

    //검색시 바텀 시트가 내려가도록 하는 코드
    private fun downToBottomSheet(){
        val bottomSheetBehavior = BottomSheetBehavior.from(activityMainBinding.bottomSheetInclude.root)
        bottomSheetBehavior.state = STATE_COLLAPSED
    }

}
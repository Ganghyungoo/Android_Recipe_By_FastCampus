package com.test.zenlyappproject

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.test.zenlyappproject.databinding.ActivityMapBinding
import com.test.zenlyappproject.dataclass.Person


class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var activityMapBinding: ActivityMapBinding
    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val markerMap = hashMapOf<String,Marker>()

    private val locationCallback = object:LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)

            //새로 요청된 위치 정보
            for (location in locationResult.locations){
                Log.d("testt","위도/경도 : ${location.latitude}/${location.longitude}")

                val uid = Firebase.auth.currentUser?.uid.orEmpty()

                val myLocationMap = mutableMapOf<String,Any>()
                myLocationMap["latitude"] = location.latitude
                myLocationMap["longitude"] = location.longitude

                Firebase.database.reference.child("Person").child(uid).updateChildren(myLocationMap)
            }
        }
    }

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ){permissions->
        when{
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION,false)->{
                //권한이 있는 경우
                getCurrentLocation()
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION,false)->{
                //권한이 있는 상광
                getCurrentLocation()
            }
            else->{
                //TODO 교육용 팝업을 띄운다
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMapBinding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(activityMapBinding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@MapActivity)

        requestLocationPermission()
        setUpFirebaseDatabase()

    }

    private fun getCurrentLocation(){
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,5*1000)
            .build()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermission()
            return
        }
        //권한이 있는 상태
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())

        fusedLocationClient.lastLocation.addOnSuccessListener {
            googleMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude),16.0f)
            )
        }
    }

    private fun requestLocationPermission() {
        locationPermissionRequest.launch(
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION)
        )
    }
    private fun setUpFirebaseDatabase(){
         Firebase.database.reference.child("Person").addChildEventListener(object: ChildEventListener{
             override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                 val person = snapshot.getValue(Person::class.java) ?: return
                 val uid = person.uid ?: return


                 if (markerMap[uid] == null){
                     markerMap[uid] = makeNewMarker(person) ?: return
                 }

             }

             override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                 val person = snapshot.getValue(Person::class.java) ?: return
                 val uid = person.uid ?: return

                 if (markerMap[uid] == null){
                     markerMap[uid] = makeNewMarker(person) ?: return
                 }else{
                     markerMap[uid]?.position = LatLng(person.latitude ?: 0.0,person.longitude ?: 0.0)
                     Log.d("testt","위치 업데이트중")
                 }
             }

             override fun onChildRemoved(snapshot: DataSnapshot) {
                 TODO("Not yet implemented")
             }

             override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                 TODO("Not yet implemented")
             }

             override fun onCancelled(error: DatabaseError) {
                 TODO("Not yet implemented")
             }

         })
    }

    private fun makeNewMarker(person: Person): Marker? {
        val marker = googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(person.latitude ?: 0.0,person.longitude ?:0.0))
                .title(person.name.orEmpty())
        ) ?: return null

        return marker
    }

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0

        googleMap.setMaxZoomPreference(20.0f)
        googleMap.setMinZoomPreference(10.0f)
    }

    override fun onResume() {
        getCurrentLocation()
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}
package com.test.zenlyappproject

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.RoundedCorner
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
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
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.test.zenlyappproject.databinding.ActivityMapBinding
import com.test.zenlyappproject.dataclass.Person


class MapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private lateinit var activityMapBinding: ActivityMapBinding
    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var trakingPersonUid: String = ""
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
        setUpEmojiAnimation()
        setUpFirebaseDatabase()

    }

    private fun setUpEmojiAnimation() {
        activityMapBinding.emojiLottieAnimationView.setOnClickListener {
            if (trakingPersonUid != "") {
                val lastEmoji = mutableMapOf<String, Any>()
                lastEmoji["type"] = "dafault"
                lastEmoji["lastModifier"] = System.currentTimeMillis()
                Firebase.database.reference.child("Emoji").child(trakingPersonUid)
                    .updateChildren(lastEmoji)
            }
            activityMapBinding.emojiLottieAnimationView.playAnimation()
        }
        activityMapBinding.centerLottieAnimationView.speed = 3f
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

                 if (uid == trakingPersonUid){
                     googleMap.animateCamera(
                         CameraUpdateFactory.newCameraPosition(
                             CameraPosition.Builder()
                                 .target(LatLng(person.latitude ?: 0.0, person.longitude ?: 0.0))
                                 .zoom(16.0f)
                                 .build()
                         )
                     )
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

        Firebase.database.reference.child("Emoji").child(Firebase.auth.currentUser?.uid ?: "").addChildEventListener(
            object: ChildEventListener{
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    activityMapBinding.centerLottieAnimationView.playAnimation()
                    activityMapBinding.centerLottieAnimationView.animate()
                        .scaleX(7f)
                        .scaleY(7f)
                        .alpha(0.3f)
                        .setDuration(activityMapBinding.centerLottieAnimationView.duration / 3)
                        .withEndAction{
                            activityMapBinding.centerLottieAnimationView.scaleX = 0f
                            activityMapBinding.centerLottieAnimationView.scaleY = 0f
                            activityMapBinding.centerLottieAnimationView.alpha = 1f
                        }
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    TODO("Not yet implemented")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {

                }

            }
        )
    }

    private fun makeNewMarker(person: Person): Marker? {
        val marker = googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(person.latitude ?: 0.0,person.longitude ?:0.0))
                .title(person.name.orEmpty())
        ) ?: return null

        marker.tag = person.uid
        
        Glide.with(this).asBitmap()
            .load(person.profilePhoto)
            .transform(RoundedCorners(60))
            .override(200)
            .listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>,
                    isFirstResource: Boolean,
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Bitmap,
                    model: Any,
                    target: Target<Bitmap>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean,
                ): Boolean {
                    runOnUiThread {
                        marker.setIcon( BitmapDescriptorFactory.fromBitmap(
                            resource
                        ))
                    }
                    return true
                }
            }).submit()

        return marker
    }

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0

        googleMap.setMaxZoomPreference(20.0f)
        googleMap.setMinZoomPreference(10.0f)

        googleMap.setOnMarkerClickListener(this@MapActivity)

        googleMap.setOnMapClickListener {
            trakingPersonUid = ""
        }
    }

    override fun onResume() {
        getCurrentLocation()
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        val bottomSheetBehavior = BottomSheetBehavior.from(activityMapBinding.emojiBottomSheetLayout)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        trakingPersonUid = p0.tag as String
        return false
    }
}
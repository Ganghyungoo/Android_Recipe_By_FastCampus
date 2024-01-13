package com.test.chattingappproject.ui

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.test.chattingappproject.R
import com.test.chattingappproject.databinding.ActivityMainBinding
import com.test.chattingappproject.ui.home.chat_room.ChatRoomListFragment
import com.test.chattingappproject.ui.home.my_page.MyPageFragment
import com.test.chattingappproject.ui.home.user_list.UserListFragment
import com.test.chattingappproject.ui.sign.LoginActivity

class MainActivity : AppCompatActivity() {
    lateinit var activtyMainBinding: ActivityMainBinding

    companion object{
        val USER_LIST_FRAGMENT = UserListFragment()
        val CHATROOM_LIST_FRAGMENT = ChatRoomListFragment()
        val MY_PAGE_FRAGMENT = MyPageFragment()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activtyMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activtyMainBinding.root)

        val currentUser = Firebase.auth.currentUser

        //로그인을 한적이 없으면
        if (currentUser == null){
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }

        askNotificationPermission()
        activtyMainBinding.mainBottomNavigationBar.selectedItemId = R.id.userList
        replaceFragment(USER_LIST_FRAGMENT)
        activtyMainBinding.mainBottomNavigationBar.setOnItemSelectedListener {
            when(it.itemId){
                R.id.userList ->{
                    replaceFragment(USER_LIST_FRAGMENT)
                    return@setOnItemSelectedListener true
                }
                R.id.chatRoomList ->{
                    replaceFragment(CHATROOM_LIST_FRAGMENT)
                    return@setOnItemSelectedListener true
                }
                R.id.myInfo ->{
                    replaceFragment(MY_PAGE_FRAGMENT)
                    return@setOnItemSelectedListener true
                }
                else->{
                    return@setOnItemSelectedListener false
                }
            }
        }
    }

    private fun replaceFragment(fragment:Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.mainFrameLayout, fragment)
            commit()
        }
    }
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            //알림 권한이 없음...
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                showRequestPermissionRationalDialog()
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun showRequestPermissionRationalDialog(){
        AlertDialog.Builder(this)
            .setMessage("알림 권한이 없으면 알림을 받을 수 없습니다")
            .setPositiveButton("권한 허용하기"){ dialogInterface: DialogInterface, i: Int ->
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
            .setNegativeButton("취소"){ dialogInterface: DialogInterface, i: Int ->
                dialogInterface.cancel()
            }
            .show()
    }

}

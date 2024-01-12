package com.test.chattingappproject.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
        if (currentUser == null){
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }
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
}

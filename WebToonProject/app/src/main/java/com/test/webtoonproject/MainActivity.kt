package com.test.webtoonproject

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.test.webtoonproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var activityMainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)

        activityMainBinding.run {
            viewPager.run {
                adapter = ViewPagerAdapter(this@MainActivity)
            }

            TabLayoutMediator(tabLayout,viewPager){ tab, position ->
                 run{
                     tab.text = "position $position"
                 }
            }.attach()
        }
    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.fragments[activityMainBinding.viewPager.currentItem]
        if (currentFragment is WebViewFragment){
            if (currentFragment.canGoBack()){
                currentFragment.goBack()
            }else{
                super.onBackPressed()
            }
        }else{
            super.onBackPressed()
        }
    }
}
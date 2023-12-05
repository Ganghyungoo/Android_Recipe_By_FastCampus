package com.test.webtoonproject

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.test.webtoonproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(),TabNameChangedListener {
    lateinit var activityMainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)

        val tabNameSharedPref = getSharedPreferences("TabName",Context.MODE_PRIVATE)
        val tab0 = tabNameSharedPref.getString("tab0_name","월요 웹툰")
        val tab1 =tabNameSharedPref.getString("tab1_name","회요 웹툰")
        val tab2 =tabNameSharedPref.getString("tab2_name","수요 웹툰")

        activityMainBinding.run {
            viewPager.run {
                adapter = ViewPagerAdapter(this@MainActivity)
            }

            TabLayoutMediator(tabLayout,viewPager){ tab, position ->
                 run{
                     when(position){
                         0 -> tab.text = tab0
                         1 -> tab.text = tab1
                         2 -> tab.text = tab2
                         else -> tab.text = "알 수 없음"
                     }
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

    override fun tabNameChanged(position: Int, name: String) {
        val tab = activityMainBinding.tabLayout.getTabAt(position)
        tab?.text = name
    }
}
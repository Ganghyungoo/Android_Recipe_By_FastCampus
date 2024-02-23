package com.test.searchmediaproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.test.searchmediaproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding:ActivityMainBinding
    private val searchFragment = SearchFragment()
    private val fragmentList = listOf(searchFragment,FavoriteFragment())
    private val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle, fragmentList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
    }

    fun initView(){
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
            view = this@MainActivity
            viewPager.adapter = adapter

            TabLayoutMediator(tabLayout,viewPager){ tab: TabLayout.Tab, position: Int ->
                tab.text = when(fragmentList[position]){
                    is SearchFragment -> {
                        "검색 결과"
                    }
                    is FavoriteFragment -> {
                        "즐겨 찾기"
                    }
                    else -> {
                        "알 수 없음"
                    }
                }
            }.attach()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        (menu.findItem(R.id.search).actionView as SearchView).apply {
            setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    TODO("Not yet implemented")
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    TODO("Not yet implemented")
                }

            })
        }
        return true
    }
}
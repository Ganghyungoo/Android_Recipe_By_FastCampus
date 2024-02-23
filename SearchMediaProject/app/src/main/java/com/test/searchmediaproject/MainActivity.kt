package com.test.searchmediaproject

import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.test.searchmediaproject.databinding.ActivityMainBinding
import com.test.searchmediaproject.ui.display_favorite.FavoriteFragment
import com.test.searchmediaproject.ui.display_search.SearchFragment
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding

    private val searchFragment = SearchFragment()
    private val fragmentList = listOf(searchFragment, FavoriteFragment())
    private val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle, fragmentList)

    private var observableTextQuery: Disposable? = null

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

        observableTextQuery = Observable.create { emitter:ObservableEmitter<String>? ->
            (menu.findItem(R.id.search).actionView as SearchView).apply {
                setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(p0: String): Boolean {
                        emitter?.onNext(p0)
                        return false
                    }

                    override fun onQueryTextChange(p0: String): Boolean {
                        activityMainBinding.viewPager.setCurrentItem(0,true)
                        emitter?.onNext(p0)
                        return false
                    }

                })
            }
        }
            .debounce(500,TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                searchFragment.searchKeyword(it)
            }




        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        this.observableTextQuery?.dispose()
        observableTextQuery = null
    }
}
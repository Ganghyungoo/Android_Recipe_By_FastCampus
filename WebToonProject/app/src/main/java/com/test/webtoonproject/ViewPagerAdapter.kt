package com.test.webtoonproject

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(val mainActivity: MainActivity): FragmentStateAdapter(mainActivity){

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->{
                 WebViewFragment(position,"https://comic.naver.com/webtoon?tab=mon")
            }
            1->{
                 WebViewFragment(position,"https://comic.naver.com/webtoon?tab=tue")
            }
            2->{
                 WebViewFragment(position,"https://comic.naver.com/webtoon?tab=wed")
            }
            else->{
                 WebViewFragment(position,"https://comic.naver.com/webtoon?tab=mon")
            }
        }
    }
}
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
                 WebViewFragment(position,"https://comic.naver.com/webtoon?tab=mon").apply {
                     tabNameChangedListener = mainActivity
                 }
            }
            1->{
                 WebViewFragment(position,"https://comic.naver.com/webtoon?tab=tue").apply {
                     tabNameChangedListener = mainActivity
                 }
            }
            2->{
                 WebViewFragment(position,"https://comic.naver.com/webtoon?tab=wed").apply {
                     tabNameChangedListener = mainActivity
                 }
            }
            else->{
                 WebViewFragment(position,"https://comic.naver.com/webtoon?tab=mon").apply {
                     tabNameChangedListener = mainActivity
                 }
            }
        }
    }
}
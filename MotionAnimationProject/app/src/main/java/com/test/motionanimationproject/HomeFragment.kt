package com.test.motionanimationproject

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.test.motionanimationproject.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        fragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

        //파일 데이터를 읽는 작업
        val homeData = context?.readData() ?: return fragmentHomeBinding.root
        //툴바 닉네임 설정
        fragmentHomeBinding.toolBarTitleTextView.text = getString(R.string.toolbar_title_text, homeData.user.nickName)
        fragmentHomeBinding.starCountTextView.text = getString(R.string.toolbar_star_count, homeData.user.starCount, homeData.user.totalCount)

        fragmentHomeBinding.progressHorizontal.max = homeData.user.totalCount
        fragmentHomeBinding.progressHorizontal.progress = homeData.user.starCount

        Glide.with(fragmentHomeBinding.toolBarImageView)
            .load(homeData.appBarImage)
            .into(fragmentHomeBinding.toolBarImageView)

        Log.d("testt", homeData.appBarImage)
        return fragmentHomeBinding.root
    }


}
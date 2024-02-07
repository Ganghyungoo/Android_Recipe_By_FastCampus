package com.test.motionanimationproject

import android.animation.ValueAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.bumptech.glide.Glide
import com.test.motionanimationproject.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var fragmentHomeBinding: FragmentHomeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Inflate the layout for this fragment
        fragmentHomeBinding = FragmentHomeBinding.bind(view)

        //파일 데이터를 읽는 작업
        val homeData =
            context?.readData("home.json", Home::class.java) ?: return
        val menuData =
            context?.readData("menu.json", Menu::class.java) ?: return
        initToolbar(homeData)
        //커피 추천 메뉴
        initCoffeeRecommend(homeData, menuData)
        //베너 설정
        initBanner(homeData)
        //음식(디저트) 추천 메뉴
        initFoodRecommend(menuData)
        //플로팅 버튼 설정
        initFloatingButtonState()



    }

    private fun initFloatingButtonState() {
        fragmentHomeBinding.scrollView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            if (scrollY == 0) {
                fragmentHomeBinding.floatingActionButton.extend()
            } else {
                fragmentHomeBinding.floatingActionButton.shrink()
            }
        }
    }

    private fun initFoodRecommend(menuData: Menu) {
        fragmentHomeBinding.foodMenuList.titleTextView.text = getString(R.string.food_title)
        menuData.food.forEach { food ->
            fragmentHomeBinding.foodMenuList.menuLayout.addView(
                MenuView(context = requireContext()).apply {
                    setTitle(food.name)
                    setImageView(food.image)
                }
            )
        }
    }

    private fun initBanner(homeData: Home) {
        fragmentHomeBinding.bannerImageViewLayout.bannerImageView.apply {
            Glide.with(this)
                .load(homeData.banner.image)
                .into(this)
            this.contentDescription = homeData.banner.contentDescription
        }
    }

    private fun initCoffeeRecommend(
        homeData: Home,
        menuData: Menu,
    ) {
        fragmentHomeBinding.recommendMenuList.titleTextView.text =
            getString(R.string.recommend_title, homeData.user.nickName)
        menuData.coffee.forEach { coffee ->
            fragmentHomeBinding.recommendMenuList.menuLayout.addView(
                MenuView(context = requireContext()).apply {
                    setTitle(coffee.name)
                    setImageView(coffee.image)
                }
            )
        }
    }

    private fun initToolbar(homeData: Home) {
        //툴바 설정
        fragmentHomeBinding.toolBarTitleTextView.text =
            getString(R.string.toolbar_title_text, homeData.user.nickName)
        fragmentHomeBinding.starCountTextView.text = getString(
            R.string.toolbar_star_count,
            homeData.user.starCount,
            homeData.user.totalCount
        )
        fragmentHomeBinding.progressHorizontal.max = homeData.user.totalCount
        fragmentHomeBinding.progressHorizontal.progress = homeData.user.starCount

        Glide.with(fragmentHomeBinding.toolBarImageView)
            .load(homeData.appBarImage)
            .into(fragmentHomeBinding.toolBarImageView)

        ValueAnimator.ofInt(0,homeData.user.starCount).apply {
            duration = 1000
            addUpdateListener {
                fragmentHomeBinding.progressHorizontal.progress = it.animatedValue as Int
            }
            start()
        }
    }


}
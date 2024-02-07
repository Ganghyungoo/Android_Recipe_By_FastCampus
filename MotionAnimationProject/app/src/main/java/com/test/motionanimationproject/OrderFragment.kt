package com.test.motionanimationproject

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.motionanimationproject.databinding.FragmentHomeBinding
import com.test.motionanimationproject.databinding.FragmentOrderBinding
import kotlin.math.abs

class OrderFragment : Fragment() {
    private lateinit var fragmentHomeBinding: FragmentOrderBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        fragmentHomeBinding = FragmentOrderBinding.inflate(layoutInflater)

        val menuAdapter = MenuAdapter()

        val menuData = context?.readData("menu.json", Menu::class.java) ?: return fragmentHomeBinding.root
        menuAdapter.apply {
            submitList(menuData.coffee)
        }

        fragmentHomeBinding.orderRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = menuAdapter
        }

        fragmentHomeBinding.toolbarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val seekPosition = abs(verticalOffset) / appBarLayout.totalScrollRange.toFloat()
            fragmentHomeBinding.orderMotionLayout.progress = seekPosition
            Log.d("orderToolbar","toolBarTotalRange:${appBarLayout.totalScrollRange} / $verticalOffset")
        }
        return fragmentHomeBinding.root
    }

}
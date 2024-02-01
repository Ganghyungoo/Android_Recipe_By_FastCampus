package com.test.motionanimationproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.motionanimationproject.databinding.FragmentHomeBinding
import com.test.motionanimationproject.databinding.FragmentOrderBinding

class OrderFragment : Fragment() {
    private lateinit var fragmentHomeBinding: FragmentOrderBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        fragmentHomeBinding = FragmentOrderBinding.inflate(layoutInflater)
        return fragmentHomeBinding.root
    }

}
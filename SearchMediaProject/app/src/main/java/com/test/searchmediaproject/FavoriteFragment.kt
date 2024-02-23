package com.test.searchmediaproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.test.searchmediaproject.databinding.FragmentFavoriteBinding
import com.test.searchmediaproject.databinding.FragmentSearchBinding

open class FavoriteFragment:Fragment() {
    private var fragmentFavoriteBinding: FragmentFavoriteBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentFavoriteBinding.inflate(inflater, container, false).apply {
            fragmentFavoriteBinding = this
        }.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentFavoriteBinding = null
    }
}
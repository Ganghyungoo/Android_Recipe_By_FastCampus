package com.test.searchmediaproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.test.searchmediaproject.databinding.FragmentSearchBinding
import com.test.searchmediaproject.list.ListAdapter

open class SearchFragment:Fragment() {
    private var fragmentSearchBinding: FragmentSearchBinding? = null

    private val adapter by lazy { ListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentSearchBinding.inflate(inflater, container, false).apply {
            fragmentSearchBinding = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentSearchBinding?.apply {
            recyclerView.adapter = adapter
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        fragmentSearchBinding = null
    }
}
package com.test.tomorrowhouseappproject.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.test.tomorrowhouseappproject.R
import com.test.tomorrowhouseappproject.databinding.FragmentHomeBinding

class HomeFragment: Fragment(R.layout.fragment_home) {
    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)
        setUpWriteButton()
        return fragmentHomeBinding.root
    }
    private fun setUpWriteButton(){
        fragmentHomeBinding.writeFloatingButton.setOnClickListener {
            if (Firebase.auth.currentUser != null){
                val action = HomeFragmentDirections.actionHomeFragmentToWriteArticleFragment()
                findNavController().navigate(action)
            }else{
                Snackbar.make(fragmentHomeBinding.root,"로그인이 필요한 작업입니다",Snackbar.LENGTH_SHORT).show()
            }

        }
    }
}

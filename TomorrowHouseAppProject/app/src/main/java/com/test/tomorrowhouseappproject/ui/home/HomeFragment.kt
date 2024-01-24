package com.test.tomorrowhouseappproject.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.test.tomorrowhouseappproject.R
import com.test.tomorrowhouseappproject.adapter.HomeArticleAdapter
import com.test.tomorrowhouseappproject.databinding.FragmentHomeBinding
import com.test.tomorrowhouseappproject.dataclass.ArticleModel

class HomeFragment: Fragment(R.layout.fragment_home) {
    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    private lateinit var homeArticleAdapter: HomeArticleAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)
        setUpWriteButton()
        homeArticleAdapter = HomeArticleAdapter {
            //상세화면으로 전환
        }

        fragmentHomeBinding.homeRecyclerView.apply {
            adapter = homeArticleAdapter
            layoutManager = GridLayoutManager(this@HomeFragment.context,2)
        }

        Firebase.firestore.collection("articles").get()
            .addOnSuccessListener { documents->
                val list = documents.map {document->
                    document.toObject<ArticleModel>()
                }
                homeArticleAdapter.submitList(list)
            }.addOnFailureListener {

            }



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

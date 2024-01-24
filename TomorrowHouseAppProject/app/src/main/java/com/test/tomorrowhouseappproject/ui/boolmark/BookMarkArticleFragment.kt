package com.test.tomorrowhouseappproject.ui.boolmark

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.test.tomorrowhouseappproject.adapter.BookMarkArticleAdapter
import com.test.tomorrowhouseappproject.databinding.FragmentBookMarkArticleBinding
import com.test.tomorrowhouseappproject.dataclass.ArticleModel

class BookMarkArticleFragment : Fragment() {
    private lateinit var fragmentBookMarkArticleBinding: FragmentBookMarkArticleBinding
    private lateinit var bookMarkArticleAdapter: BookMarkArticleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        fragmentBookMarkArticleBinding = FragmentBookMarkArticleBinding.inflate(layoutInflater)

        //툴바 뒤로가기
        fragmentBookMarkArticleBinding.toolbar.setNavigationOnClickListener {
            findNavController().navigate(BookMarkArticleFragmentDirections.actionBookMarkArticleFragmentToHomeFragment())
        }



        //북마크 어뎁터 생성
        bookMarkArticleAdapter = BookMarkArticleAdapter(){
            //아이템클릭했을때
            findNavController().navigate(BookMarkArticleFragmentDirections.actionBookMarkArticleFragmentToArticleFragment(it.articleId.orEmpty()))
        }
        fragmentBookMarkArticleBinding.bookMarkRecyclerView.apply {
            layoutManager = GridLayoutManager(context,2)
            adapter = bookMarkArticleAdapter
        }

        val uid = Firebase.auth.currentUser?.uid.orEmpty()
        Firebase.firestore.collection("bookmarks")
            .document(uid)
            .get()
            .addOnSuccessListener{
                val list = it.get("articleIds") as List<*>
                if (list.isNotEmpty()){
                   val list =  Firebase.firestore.collection("articles")
                        .whereIn("articleId",list)
                        .get()
                        .addOnSuccessListener {result->
                            bookMarkArticleAdapter.submitList(result.map {documents-> documents.toObject() })
                        }.addOnFailureListener {
                            it.printStackTrace()
                        }
                }
            }.addOnFailureListener {
                it.printStackTrace()
            }
        return fragmentBookMarkArticleBinding.root
    }
}
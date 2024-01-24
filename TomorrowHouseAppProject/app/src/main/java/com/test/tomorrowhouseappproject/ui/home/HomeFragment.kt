package com.test.tomorrowhouseappproject.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.test.tomorrowhouseappproject.R
import com.test.tomorrowhouseappproject.adapter.HomeArticleAdapter
import com.test.tomorrowhouseappproject.databinding.FragmentHomeBinding
import com.test.tomorrowhouseappproject.dataclass.ArticleItem
import com.test.tomorrowhouseappproject.dataclass.ArticleModel

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    private lateinit var homeArticleAdapter: HomeArticleAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        fragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

        setUpWriteButton()

        setUpBookMarkButton()

        setUpRecyclerView()

        fetchFireStoreData()



        return fragmentHomeBinding.root
    }

    private fun fetchFireStoreData() {
        val myUid = Firebase.auth.currentUser?.uid ?: return
        Firebase.firestore.collection("bookmarks").document(myUid)
            .get().addOnSuccessListener { bookMarkdocument ->
                val bookMarkList: List<Any?>
                if (bookMarkdocument.exists()) {
                    bookMarkList = bookMarkdocument.get("articleIds") as List<*>
                } else {
                    bookMarkList = emptyList()
                }

                Firebase.firestore.collection("articles").get()
                    .addOnSuccessListener { documents ->
                        val list = documents.map { document ->
                            val obj = document.toObject<ArticleModel>()
                            if (bookMarkList.contains(obj.articleId)) {
                                return@map ArticleItem(
                                    obj.articleId ?: "",
                                    obj.createAt ?: 0.0.toLong(),
                                    obj.description ?: "",
                                    obj.imageUrl ?: "",
                                    true
                                )
                            } else {
                                return@map ArticleItem(
                                    obj.articleId ?: "",
                                    obj.createAt ?: 0.0.toLong(),
                                    obj.description ?: "",
                                    obj.imageUrl ?: "",
                                    false
                                )
                            }
                        }
                        Log.d("testt", "$list")
                        homeArticleAdapter.submitList(list)
                    }.addOnFailureListener {
                        Log.d("testt", "통신실패")
                    }
            }
    }

    private fun setUpRecyclerView() {
        homeArticleAdapter = HomeArticleAdapter(
            onItemClicked = {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToArticleFragment(
                    it.articleId.orEmpty()
                )
            )
        },
            bookMarkClicked = { articleId, isBookMark ->
                val myUid = Firebase.auth.currentUser?.uid.orEmpty()
                Firebase.firestore.collection("bookmarks").document(myUid)
                    .update("articleIds",
                        if (isBookMark){
                            FieldValue.arrayUnion(articleId)
                        }else{
                            FieldValue.arrayRemove(articleId)
                        }
                    ).addOnFailureListener {
                        if (it is FirebaseFirestoreException && it.code == FirebaseFirestoreException.Code.NOT_FOUND){
                            if (isBookMark){
                                Firebase.firestore.collection("bookmarks").document(myUid).set(
                                    hashMapOf("articleIds" to listOf(articleId))
                                )
                            }
                        }
                    }
            }
        )

        fragmentHomeBinding.homeRecyclerView.apply {
            adapter = homeArticleAdapter
            layoutManager = GridLayoutManager(this@HomeFragment.context, 2)
        }
    }

    private fun setUpBookMarkButton() {
        fragmentHomeBinding.toobar.menu.getItem(0).setOnMenuItemClickListener {
            val myUid = Firebase.auth.currentUser?.uid ?: return@setOnMenuItemClickListener false
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToBookMarkArticleFragment())
            false
        }
    }

    private fun setUpWriteButton() {
        fragmentHomeBinding.writeFloatingButton.setOnClickListener {
            if (Firebase.auth.currentUser != null) {
                val action = HomeFragmentDirections.actionHomeFragmentToWriteArticleFragment()
                findNavController().navigate(action)
            } else {
                Snackbar.make(fragmentHomeBinding.root, "로그인이 필요한 작업입니다", Snackbar.LENGTH_SHORT)
                    .show()
            }

        }
    }
}

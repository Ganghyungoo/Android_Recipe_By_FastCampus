package com.test.tomorrowhouseappproject.ui.article

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.test.tomorrowhouseappproject.R
import com.test.tomorrowhouseappproject.databinding.FragmentArticleBinding
import com.test.tomorrowhouseappproject.dataclass.ArticleModel

class ArticleFragment : Fragment() {
    private lateinit var fragmentArticleBinding: FragmentArticleBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        fragmentArticleBinding = FragmentArticleBinding.inflate(layoutInflater)
/*        val articleId = args.articleId

        Firebase.firestore.collection("articles").document(articleId)
            .get()
            .addOnSuccessListener {
                val model = it.toObject<ArticleModel>()
                fragmentArticleBinding.descriptionTextView.text = model?.description
                Glide.with(fragmentArticleBinding.thumnailImageView)
                    .load(model?.imageUrl)
                    .into(fragmentArticleBinding.thumnailImageView)

            }.addOnFailureListener {

            }*/



        return fragmentArticleBinding.root
    }
}
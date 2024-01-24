package com.test.tomorrowhouseappproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.tomorrowhouseappproject.databinding.ItemArticleBinding
import com.test.tomorrowhouseappproject.dataclass.ArticleModel

class HomeArticleAdapter(val onItemClicked: (ArticleModel) -> Unit): ListAdapter<ArticleModel, HomeArticleAdapter.ViewHolder>(diffUtil) {
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ArticleModel>() {
            override fun areItemsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
                return newItem.articleId == oldItem.articleId
            }

            override fun areContentsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
                return newItem == oldItem
            }

        }
    }

    inner class ViewHolder(val itemArticleBinding: ItemArticleBinding) :
        RecyclerView.ViewHolder(itemArticleBinding.root) {
        fun bind(articleModel: ArticleModel) {
            Glide.with(itemArticleBinding.thumnailImageView)
                .load(articleModel.imageUrl)
                .into(itemArticleBinding.thumnailImageView)

            itemArticleBinding.descriptionTextView.text = articleModel.description

            itemArticleBinding.root.setOnClickListener {
                onItemClicked(articleModel)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemArticleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

}
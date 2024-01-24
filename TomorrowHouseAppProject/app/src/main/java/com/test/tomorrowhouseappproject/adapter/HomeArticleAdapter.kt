package com.test.tomorrowhouseappproject.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.tomorrowhouseappproject.R
import com.test.tomorrowhouseappproject.databinding.ItemArticleBinding
import com.test.tomorrowhouseappproject.dataclass.ArticleItem
import com.test.tomorrowhouseappproject.dataclass.ArticleModel

class HomeArticleAdapter(
    val onItemClicked: (ArticleItem) -> Unit,
    val bookMarkClicked: (String,Boolean) -> Unit
): ListAdapter<ArticleItem, HomeArticleAdapter.ViewHolder>(diffUtil) {
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ArticleItem>() {
            override fun areItemsTheSame(oldItem: ArticleItem, newItem: ArticleItem): Boolean {
                return newItem.articleId == oldItem.articleId
            }

            override fun areContentsTheSame(oldItem: ArticleItem, newItem: ArticleItem): Boolean {
                return newItem == oldItem
            }

        }
    }

    inner class ViewHolder(val itemArticleBinding: ItemArticleBinding) :
        RecyclerView.ViewHolder(itemArticleBinding.root) {
        fun bind(articleItem: ArticleItem) {
            Glide.with(itemArticleBinding.thumnailImageView)
                .load(articleItem.imageUrl)
                .into(itemArticleBinding.thumnailImageView)

            itemArticleBinding.descriptionTextView.text = articleItem.description
            if (articleItem.isBookMark){
                itemArticleBinding.bookMarkImageButton.setImageResource(R.drawable.baseline_bookmark_24)
            }else{
                itemArticleBinding.bookMarkImageButton.setImageResource(R.drawable.baseline_bookmark_border_24)
            }

            itemArticleBinding.bookMarkImageButton.setOnClickListener {
                bookMarkClicked.invoke(articleItem.articleId, !articleItem.isBookMark)

                articleItem.isBookMark = !articleItem.isBookMark
                if (articleItem.isBookMark){
                    itemArticleBinding.bookMarkImageButton.setImageResource(R.drawable.baseline_bookmark_24)
                }else{
                    itemArticleBinding.bookMarkImageButton.setImageResource(R.drawable.baseline_bookmark_border_24)
                }
            }

            itemArticleBinding.root.setOnClickListener {
                onItemClicked(articleItem)
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
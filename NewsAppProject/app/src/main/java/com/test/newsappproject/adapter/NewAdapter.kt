package com.test.newsappproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.test.newsappproject.dataModel.NewsItem
import com.test.newsappproject.databinding.ItemNewsBinding

class NewsAdapter : ListAdapter<NewsItem, NewsAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val itemNewsBinding: ItemNewsBinding) :
        RecyclerView.ViewHolder(itemNewsBinding.root) {

        fun binding(item: NewsItem) {
            itemNewsBinding.newsTitle.text = item.title
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemNewsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<NewsItem>() {
            override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
                return oldItem == newItem
            }
        }
    }


}
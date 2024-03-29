package com.test.searchmediaproject.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.test.searchmediaproject.databinding.ItemImageBinding
import com.test.searchmediaproject.databinding.ItemVideoBinding
import com.test.searchmediaproject.list.viewholder.ImageViewHolder
import com.test.searchmediaproject.list.viewholder.VideoViewHolder
import com.test.searchmediaproject.model.ImageItem
import com.test.searchmediaproject.model.ListItem

class ListAdapter(private val handler:ItemHandler? = null) : ListAdapter<ListItem, RecyclerView.ViewHolder>(diffUtil) {

    override fun getItemViewType(position: Int): Int {
        return if (currentList[position] is ImageItem) {
            IMAGE
        } else {
            VIDEO
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == IMAGE) {
            ImageViewHolder(ItemImageBinding.inflate(LayoutInflater.from(parent.context),parent,false),handler)
        } else {
            VideoViewHolder(ItemVideoBinding.inflate(LayoutInflater.from(parent.context),parent,false),handler)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == IMAGE){
            (holder as ImageViewHolder).bind(currentList[position])
        }else{
            (holder as VideoViewHolder).bind(currentList[position])
        }
    }

    companion object {
        private const val IMAGE = 0
        private const val VIDEO = 1
        private val diffUtil = object : DiffUtil.ItemCallback<ListItem>() {
            override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
                return oldItem.thumbnailUrl == newItem.thumbnailUrl
            }

            override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
                return oldItem == newItem
            }

        }
    }
}
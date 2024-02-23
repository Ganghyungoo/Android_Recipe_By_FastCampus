package com.test.searchmediaproject.list.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.test.searchmediaproject.databinding.ItemVideoBinding
import com.test.searchmediaproject.model.ImageItem
import com.test.searchmediaproject.model.ListItem
import com.test.searchmediaproject.model.VideoItem

class VideoViewHolder(
    private val itemVideoBinding: ItemVideoBinding
):RecyclerView.ViewHolder(itemVideoBinding.root){
    fun bind(item: ListItem){
        item as VideoItem
        itemVideoBinding.item = item
    }
}
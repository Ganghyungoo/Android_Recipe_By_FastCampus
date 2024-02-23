package com.test.searchmediaproject.list.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.test.searchmediaproject.databinding.ItemImageBinding
import com.test.searchmediaproject.model.ImageItem
import com.test.searchmediaproject.model.ListItem

class ImageViewHolder(
    private val itemImageBinding: ItemImageBinding
):RecyclerView.ViewHolder(itemImageBinding.root){
    fun bind(item: ListItem){
        item as ImageItem
        itemImageBinding.item = item
    }
}
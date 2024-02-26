package com.test.searchmediaproject.list.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.test.searchmediaproject.databinding.ItemImageBinding
import com.test.searchmediaproject.list.ItemHandler
import com.test.searchmediaproject.model.ImageItem
import com.test.searchmediaproject.model.ListItem

class ImageViewHolder(
    private val itemImageBinding: ItemImageBinding,
    private val handler:ItemHandler? = null
):RecyclerView.ViewHolder(itemImageBinding.root){
    fun bind(item: ListItem){
        item as ImageItem
        itemImageBinding.item = item
        itemImageBinding.handler = handler
    }
}
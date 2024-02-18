package com.test.walletproject

import androidx.recyclerview.widget.RecyclerView
import com.test.walletproject.databinding.ItemDetailBinding
import com.test.walletproject.model.DetailItem

class DetailViewHolder(private val itemDetailBinding: ItemDetailBinding):RecyclerView.ViewHolder(itemDetailBinding.root) {
    fun bind(item: DetailItem){
        itemDetailBinding.item = item
    }
}
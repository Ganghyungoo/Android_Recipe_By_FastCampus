package com.test.shoppingmallproject.viewholder

import com.test.shoppingmallproject.ListAdapter
import com.test.shoppingmallproject.databinding.ItemHorizontalBinding
import com.test.shoppingmallproject.model.Horizontal
import com.test.shoppingmallproject.model.ListItem

class HorizontalViewHolder(private val binging:ItemHorizontalBinding): BindingViewHolder<ItemHorizontalBinding>(binging) {
    private val adapter = ListAdapter()

    init {
        binging.listView.adapter = adapter
    }

    override fun bind(item: ListItem) {
        super.bind(item)
        item as Horizontal
        binging.titleTextView.text = item.title
        adapter.submitList(item.items)
    }
}
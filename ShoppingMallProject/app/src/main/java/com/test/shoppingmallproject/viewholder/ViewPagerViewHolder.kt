package com.test.shoppingmallproject.viewholder

import com.test.shoppingmallproject.ListAdapter
import com.test.shoppingmallproject.databinding.ItemViewpagerBinding
import com.test.shoppingmallproject.model.ListItem
import com.test.shoppingmallproject.model.ViewPager

class ViewPagerViewHolder(binding:ItemViewpagerBinding): BindingViewHolder<ItemViewpagerBinding>(binding){

    private val adapter = ListAdapter()

    init {
        binding.viewpager.adapter = adapter
    }

    override fun bind(item: ListItem) {
        super.bind(item)
        item as ViewPager
        adapter.submitList(item.items)
    }
}
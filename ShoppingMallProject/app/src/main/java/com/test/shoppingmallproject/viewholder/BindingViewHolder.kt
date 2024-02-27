package com.test.shoppingmallproject.viewholder

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.test.shoppingmallproject.model.ListItem

abstract class BindingViewHolder<VB : ViewDataBinding>(private val binding: VB) :
    RecyclerView.ViewHolder(binding.root) {

    protected var item: ListItem? = null

    open fun bind(item: ListItem) {
        this.item = item
        binding.setVariable(BR.item, this.item)
    }
}
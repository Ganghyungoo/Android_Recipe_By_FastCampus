package com.test.shoppingmallproject

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.test.shoppingmallproject.model.ListItem
import com.test.shoppingmallproject.viewholder.BindingViewHolder
import com.test.shoppingmallproject.viewholder.ViewHolderGenerator

class PagingListAdapter: PagingDataAdapter<ListItem, BindingViewHolder<*>>(DiffCallback()) {
    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return item?.viewType?.ordinal ?: -1
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<*> {
        return ViewHolderGenerator.get(parent, viewType)
    }

    override fun onBindViewHolder(holder: BindingViewHolder<*>, position: Int) {
        val item = getItem(position)
        if (item != null){
            holder.bind(item)
        }
    }
}
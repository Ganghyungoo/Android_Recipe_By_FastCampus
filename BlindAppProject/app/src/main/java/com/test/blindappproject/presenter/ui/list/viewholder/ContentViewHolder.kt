package com.test.blindappproject.presenter.ui.list.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.test.blindappproject.databinding.ItemContentBinding
import com.test.blindappproject.domain.model.Content
import com.test.blindappproject.presenter.ui.MainActivity

class ContentViewHolder(
    private val binding: ItemContentBinding,
    private val handler: MainActivity.Handler
    ) :
    RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Content) {
            binding.item = item
            binding.handler = handler
        }
}
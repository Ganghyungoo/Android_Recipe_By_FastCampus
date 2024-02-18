package com.test.walletproject.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.test.walletproject.DetailViewHolder
import com.test.walletproject.databinding.ItemDetailBinding
import com.test.walletproject.model.DetailItem

class DetailListAdapter: ListAdapter<DetailItem, DetailViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        return DetailViewHolder(ItemDetailBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
    companion object{
        val diffUtil = object:DiffUtil.ItemCallback<DetailItem>(){
            override fun areItemsTheSame(oldItem: DetailItem, newItem: DetailItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: DetailItem, newItem: DetailItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
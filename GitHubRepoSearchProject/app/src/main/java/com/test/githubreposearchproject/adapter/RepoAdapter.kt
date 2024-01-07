package com.test.githubreposearchproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.test.githubreposearchproject.databinding.ItemRepoBinding
import com.test.githubreposearchproject.datamodel.Repo

class RepoAdapter : ListAdapter<Repo, RepoAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val itemRepoBinding: ItemRepoBinding) :
        RecyclerView.ViewHolder(itemRepoBinding.root) {

            fun bind(item:Repo){
                itemRepoBinding.repoNameTextView.text = item.name
                itemRepoBinding.descriptionTextView.text = item.description
                itemRepoBinding.forkTextView.text = item.forkCount.toString()
                itemRepoBinding.starCountTextView.text = item.starCount.toString()
            }
    }

    companion object {
        val diffUtil = object :DiffUtil.ItemCallback<Repo>(){
            override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemRepoBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}
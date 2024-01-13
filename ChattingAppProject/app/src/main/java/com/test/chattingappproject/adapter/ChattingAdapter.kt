package com.test.chattingappproject.adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.test.chattingappproject.dataModel.ChatModel
import com.test.chattingappproject.dataModel.UserModel
import com.test.chattingappproject.databinding.ItemChatBinding
import com.test.chattingappproject.databinding.ItemChatroomBinding

class ChattingAdapter(var otherUserModel: UserModel? = null) :
    ListAdapter<ChatModel, ChattingAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(private val itemChatBinding: ItemChatBinding) :
        RecyclerView.ViewHolder(itemChatBinding.root) {
        fun biding(item: ChatModel) {
            if (item.userId == otherUserModel?.userId){
                itemChatBinding.userNameTextView.text = otherUserModel?.userName
                itemChatBinding.chatDetailTextView.text = item.message
                itemChatBinding.chatDetailTextView.gravity = Gravity.START
                itemChatBinding.userNameTextView.visibility = View.VISIBLE
            }else{
                itemChatBinding.chatDetailTextView.text = item.message
                itemChatBinding.chatDetailTextView.gravity = Gravity.END
                itemChatBinding.userNameTextView.visibility = View.GONE
            }

        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ChatModel>() {
            override fun areItemsTheSame(oldItem: ChatModel, newItem: ChatModel): Boolean {
                return oldItem.chatId == newItem.chatId
            }

            override fun areContentsTheSame(oldItem: ChatModel, newItem: ChatModel): Boolean {
                return oldItem == newItem
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemChatBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.biding(currentList[position])
    }
}
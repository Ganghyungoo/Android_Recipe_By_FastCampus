package com.test.chattingappproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.test.chattingappproject.dataModel.ChatRoomModel
import com.test.chattingappproject.databinding.ItemChatroomBinding

class ChatRoomAdapter(private val onClick:(item:ChatRoomModel)->Unit) : ListAdapter<ChatRoomModel, ChatRoomAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val itemChatroomBinding: ItemChatroomBinding) :
        RecyclerView.ViewHolder(itemChatroomBinding.root) {
        fun biding(item: ChatRoomModel) {
            itemChatroomBinding.nickNameTextView.text = item.otherUserName
            itemChatroomBinding.lastMessageTextView.text = item.lastMessage
            itemChatroomBinding.root.setOnClickListener {
                onClick(item)
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ChatRoomModel>() {

            override fun areItemsTheSame(oldItem: ChatRoomModel, newItem: ChatRoomModel): Boolean {
               return oldItem.chatRoomId == newItem.chatRoomId
            }

            override fun areContentsTheSame(
                oldItem: ChatRoomModel,
                newItem: ChatRoomModel,
            ): Boolean {
                return oldItem == newItem
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemChatroomBinding.inflate(
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
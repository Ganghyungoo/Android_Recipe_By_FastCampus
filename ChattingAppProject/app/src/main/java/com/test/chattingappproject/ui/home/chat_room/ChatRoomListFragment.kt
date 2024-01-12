package com.test.chattingappproject.ui.home.chat_room

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.chattingappproject.R
import com.test.chattingappproject.adapter.ChatRoomAdapter
import com.test.chattingappproject.dataModel.ChatRoomModel
import com.test.chattingappproject.databinding.FragmentChatRoomListBinding
import com.test.chattingappproject.databinding.ItemChatroomBinding

class ChatRoomListFragment : Fragment() {
    private lateinit var fragmentChatroomBinding: FragmentChatRoomListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        fragmentChatroomBinding = FragmentChatRoomListBinding.inflate(layoutInflater)

        val chatRoomAdapter = ChatRoomAdapter()
        val linearLayoutManager = LinearLayoutManager(this.context)
        fragmentChatroomBinding.chatRoomRecyclerView.run {
            adapter = chatRoomAdapter
            layoutManager = linearLayoutManager
        }
        chatRoomAdapter.submitList(mutableListOf(ChatRoomModel("77","77","77")) )


        return fragmentChatroomBinding.root
    }


}
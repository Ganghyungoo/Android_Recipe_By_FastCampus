package com.test.chattingappproject.ui.home.chat_room

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.test.chattingappproject.R
import com.test.chattingappproject.adapter.ChatRoomAdapter
import com.test.chattingappproject.dataModel.ChatRoomModel
import com.test.chattingappproject.databinding.FragmentChatRoomListBinding
import com.test.chattingappproject.databinding.ItemChatroomBinding
import com.test.chattingappproject.ui.MainActivity
import com.test.chattingappproject.ui.chatting.ChatActivity
import com.test.chattingappproject.viewModel.ChatRoomViewModel

class ChatRoomListFragment : Fragment() {
    private lateinit var fragmentChatroomBinding: FragmentChatRoomListBinding
    private lateinit var chatRoomViewModel: ChatRoomViewModel
    private lateinit var chatRoomAdapter: ChatRoomAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        fragmentChatroomBinding = FragmentChatRoomListBinding.inflate(layoutInflater)
        chatRoomViewModel = ViewModelProvider(this.activity as MainActivity)[ChatRoomViewModel::class.java]

        chatRoomViewModel.chatRoomList.observe(viewLifecycleOwner){
            chatRoomAdapter.submitList(it)
        }

        chatRoomAdapter = ChatRoomAdapter{
            val intent = Intent(this@ChatRoomListFragment.context, ChatActivity::class.java)
            intent.putExtra("otherUserId",it.otherUserId)
            intent.putExtra("chatRoomId",it.chatRoomId)
            startActivity(intent)

        }
        val linearLayoutManager = LinearLayoutManager(this.context)
        fragmentChatroomBinding.chatRoomRecyclerView.run {
            adapter = chatRoomAdapter
            layoutManager = linearLayoutManager
        }

        val myId = Firebase.auth.uid ?: ":"
        chatRoomViewModel.fetchMyChatRoom(myId)
        return fragmentChatroomBinding.root
    }


}
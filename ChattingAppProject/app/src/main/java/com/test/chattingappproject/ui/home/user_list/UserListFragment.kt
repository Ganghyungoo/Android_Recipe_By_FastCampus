package com.test.chattingappproject.ui.home.user_list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.test.chattingappproject.Key.Companion.DB_CHAT_ROOMS
import com.test.chattingappproject.R
import com.test.chattingappproject.adapter.UserAdapter
import com.test.chattingappproject.dataModel.ChatRoomModel
import com.test.chattingappproject.dataModel.UserModel
import com.test.chattingappproject.databinding.FragmentUserListBinding
import com.test.chattingappproject.ui.chatting.ChatActivity
import com.test.chattingappproject.viewModel.ChatRoomViewModel
import com.test.chattingappproject.viewModel.UserListViewModel
import java.util.UUID

class UserListFragment : Fragment() {
    lateinit var fragmentUserListBinding: FragmentUserListBinding
    lateinit var userListViewModel: UserListViewModel
    lateinit var chatRoomViewModel: ChatRoomViewModel
    lateinit var userAdapter: UserAdapter
    lateinit var otherUser: UserModel
    lateinit var chatRoomDB: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        fragmentUserListBinding = FragmentUserListBinding.inflate(layoutInflater)
        userListViewModel = ViewModelProvider(this)[UserListViewModel::class.java]
        chatRoomViewModel = ViewModelProvider(this)[ChatRoomViewModel::class.java]



        userListViewModel.userList.observe(viewLifecycleOwner) {
            userAdapter.submitList(it)
        }
        chatRoomViewModel.chatRoom.observe(viewLifecycleOwner) {
            var chatRoomId = ""
            if (it != null) {
                chatRoomId = it.chatRoomId!!
            } else {
                chatRoomId = UUID.randomUUID().toString()
                val newChatRoom = ChatRoomModel(
                    chatRoomId = chatRoomId,
                    otherUserName = otherUser.userName,
                    otherUserId = otherUser.userId
                )
                chatRoomDB.setValue(newChatRoom)
            }
            val intent = Intent(this@UserListFragment.context,ChatActivity::class.java)
            intent.putExtra("otherUserId",otherUser.userId)
            intent.putExtra("chatRoomId",chatRoomId)
            startActivity(intent)
        }

        //리사이클러뷰 아이템 클릭 리스너 설정
        userAdapter = UserAdapter {
            //통신해서 현재 채팅방이 있는 지 검사후 있으면 그거 쓰고 없으면 생성 후 넘기는 작업
            otherUser = it
            val myId = Firebase.auth.uid ?: ""
            chatRoomDB = Firebase.database.reference.child(DB_CHAT_ROOMS).child(myId).child(it.userId ?: "")
            chatRoomViewModel.fetchOneChatRoom(chatRoomDB)
        }
        val linearLayoutManager = LinearLayoutManager(this@UserListFragment.context)
        fragmentUserListBinding.userRecyclerView.run {
            adapter = userAdapter
            layoutManager = linearLayoutManager
        }

        //유저 정보 끌어오기
        val myId = Firebase.auth.uid
        userListViewModel.fetchUsers(myId!!)



        return fragmentUserListBinding.root
    }
}
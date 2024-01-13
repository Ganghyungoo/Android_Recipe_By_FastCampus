package com.test.chattingappproject.ui.chatting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Contacts.SettingsColumns.KEY
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.test.chattingappproject.Key
import com.test.chattingappproject.R
import com.test.chattingappproject.adapter.ChattingAdapter
import com.test.chattingappproject.dataModel.ChatModel
import com.test.chattingappproject.databinding.ActivityChatBinding
import com.test.chattingappproject.viewModel.ChatViewModel
import com.test.chattingappproject.viewModel.MyInfoViewModel

class ChatActivity : AppCompatActivity() {
    private lateinit var activityChatBinding: ActivityChatBinding
    private lateinit var chattingAdapter: ChattingAdapter
    private lateinit var myInfoViewModel: MyInfoViewModel
    private lateinit var chatViewModel: ChatViewModel
    private var chattingList : MutableList<ChatModel> = mutableListOf()

    private var chatRoomId: String = ""
    private var otherUserId: String = ""
    private var myUserId: String = ""
    private var myUseName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityChatBinding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(activityChatBinding.root)
        myInfoViewModel = ViewModelProvider(this)[MyInfoViewModel::class.java]
        chatViewModel = ViewModelProvider(this)[ChatViewModel::class.java]

        //채팅방 유저의 정보를 감시
        myInfoViewModel.userInfo.observe(this){
            //얻어온 유저 정보가 나 자신의 정보가 아니라면 상대방 정보일 경우밖에 없음
            if (it.userId != myUserId){
                chattingAdapter.otherUserModel = it
            }else{
                myUseName = it.userName.toString()
            }
        }

        //채팅방 내부 채팅 정보 가져오는 것 감시
        chatViewModel.chatItem.observe(this){
            chattingList.add(it)
            val list = chattingList
            chattingAdapter.submitList(list)
        }

        chatRoomId = intent.getStringExtra("chatRoomId") ?: return
        otherUserId = intent.getStringExtra("otherUserId") ?: return
        myUserId = Firebase.auth.uid ?: return

        //상대방의 정보를 얻기위한 통신 메서드 호출후 옵저버 대기(닉네임)
        val otherUserDB = Firebase.database.reference.child(Key.DB_USERS).child(otherUserId)
        val myDB = Firebase.database.reference.child(Key.DB_USERS).child(myUserId)
        myInfoViewModel.fetchMyInfo(otherUserDB)
        myInfoViewModel.fetchMyInfo(myDB)


        Log.d("testt","$chatRoomId")
        chatViewModel.fetchChattingList(chatRoomId)




        //어뎁터 및 리니어 레이아웃 초기화
        chattingAdapter = ChattingAdapter()
        val linearLayoutManager = LinearLayoutManager(this)
        activityChatBinding.chattingRecyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = chattingAdapter
        }

        activityChatBinding.sendMessageButton.setOnClickListener {
            val newMessage = activityChatBinding.messageTextView.text.toString()
            if(newMessage.trim().isNotEmpty()){
                val chatItem = ChatModel(
                    message = newMessage,
                    userId = myUserId
                )
                Firebase.database.reference.child(Key.DB_CHATS).child(chatRoomId).push().apply {
                    chatItem.chatId = key
                    setValue(chatItem).addOnCompleteListener {
                        runOnUiThread {
                            activityChatBinding.messageTextView.setText("")
                        }
                    }
                }

                val updates: MutableMap<String,Any> = hashMapOf(
                    "${Key.DB_CHAT_ROOMS}/$myUserId/$otherUserId/lastMessage" to newMessage,
                    "${Key.DB_CHAT_ROOMS}/$otherUserId/$myUserId/lastMessage" to newMessage,
                    "${Key.DB_CHAT_ROOMS}/$otherUserId/$myUserId/chatRoomId" to chatRoomId,
                    "${Key.DB_CHAT_ROOMS}/$otherUserId/$myUserId/otherUserId" to myUserId,
                    "${Key.DB_CHAT_ROOMS}/$otherUserId/$myUserId/otherUserName" to myUseName,
                )
                Firebase.database.reference.updateChildren(updates)
            }

        }
    }
}
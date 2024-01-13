package com.test.chattingappproject.ui.chatting

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.test.chattingappproject.Key
import com.test.chattingappproject.Key.Companion.FCM_SERVER_KEY
import com.test.chattingappproject.adapter.ChattingAdapter
import com.test.chattingappproject.dataModel.ChatModel
import com.test.chattingappproject.databinding.ActivityChatBinding
import com.test.chattingappproject.viewModel.ChatViewModel
import com.test.chattingappproject.viewModel.MyInfoViewModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class ChatActivity : AppCompatActivity() {
    private lateinit var activityChatBinding: ActivityChatBinding
    private lateinit var chattingAdapter: ChattingAdapter
    private lateinit var myInfoViewModel: MyInfoViewModel
    private lateinit var chatViewModel: ChatViewModel
    private lateinit var linearLayoutManager :LinearLayoutManager
    private var chattingList: MutableList<ChatModel> = mutableListOf()

    private var chatRoomId: String = ""
    private var otherUserId: String = ""
    private var myUserId: String = ""
    private var myUseName: String = ""
    private var otherUserFcmToken: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityChatBinding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(activityChatBinding.root)
        myInfoViewModel = ViewModelProvider(this)[MyInfoViewModel::class.java]
        chatViewModel = ViewModelProvider(this)[ChatViewModel::class.java]

        //채팅방 유저의 정보를 감시
        myInfoViewModel.userInfo.observe(this) {
            //얻어온 유저 정보가 나 자신의 정보가 아니라면 상대방 정보일 경우밖에 없음
            if (it.userId != myUserId) {
                chattingAdapter.otherUserModel = it
                otherUserFcmToken = it.fcmToken.orEmpty()
            } else {
                myUseName = it.userName.toString()
                chatViewModel.fetchChattingList(chatRoomId)
            }
        }

        //채팅방 내부 채팅 정보 가져오는 것 감시
        chatViewModel.chatItem.observe(this) {
            Log.d("chattingUpdateFunc","$it")
            chattingList.add(it)
            val list = chattingList
            chattingAdapter.submitList(list.toMutableList())
//            linearLayoutManager.smoothScrollToPosition(activityChatBinding.chattingRecyclerView,null,chattingAdapter.itemCount+1)
        }

        chatRoomId = intent.getStringExtra("chatRoomId") ?: return
        otherUserId = intent.getStringExtra("otherUserId") ?: return
        myUserId = Firebase.auth.uid ?: return

        //상대방의 정보를 얻기위한 통신 메서드 호출후 옵저버 대기(닉네임)
        val otherUserDB = Firebase.database.reference.child(Key.DB_USERS).child(otherUserId)
        val myDB = Firebase.database.reference.child(Key.DB_USERS).child(myUserId)
        myInfoViewModel.fetchMyInfo(otherUserDB)
        myInfoViewModel.fetchMyInfo(myDB)

        //어뎁터 및 리니어 레이아웃 초기화
        chattingAdapter = ChattingAdapter()
        linearLayoutManager = LinearLayoutManager(this)
        activityChatBinding.chattingRecyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = chattingAdapter
        }

        chattingAdapter.registerAdapterDataObserver(object:RecyclerView.AdapterDataObserver(){
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)

                linearLayoutManager.smoothScrollToPosition(activityChatBinding.chattingRecyclerView,null,chattingAdapter.itemCount)
            }
        })

        activityChatBinding.sendMessageButton.setOnClickListener {
            //해당 채팅을 저장
            val newMessage = activityChatBinding.messageTextView.text.toString()
            if (newMessage.trim().isNotEmpty()) {
                val chatItem = ChatModel(
                    message = newMessage,
                    userId = myUserId
                )
                Firebase.database.reference.child(Key.DB_CHATS).child(chatRoomId).push().apply {
                    chatItem.chatId = key
                    setValue(chatItem).addOnCompleteListener {
                        activityChatBinding.messageTextView.setText("")
                        //추가가 되었으면 해당 채팅방의 최근 채팅 인자까지 업데이트해준다.
                        val updates: MutableMap<String, Any> = hashMapOf(
                            "${Key.DB_CHAT_ROOMS}/$myUserId/$otherUserId/lastMessage" to newMessage,
                            "${Key.DB_CHAT_ROOMS}/$otherUserId/$myUserId/lastMessage" to newMessage,
                            "${Key.DB_CHAT_ROOMS}/$otherUserId/$myUserId/chatRoomId" to chatRoomId,
                            "${Key.DB_CHAT_ROOMS}/$otherUserId/$myUserId/otherUserId" to myUserId,
                            "${Key.DB_CHAT_ROOMS}/$otherUserId/$myUserId/otherUserName" to myUseName,
                        )
                        //업데이트까지 완료가 되었을 때 알림을 전송.
                        Firebase.database.reference.updateChildren(updates).addOnCompleteListener {
                            //알림을 전송을 위한 메서드 호출
                            //okhttp 통신 수행
                            chatViewModel.sendFCM(myUseName,newMessage,otherUserFcmToken)
                        }
                    }
                }
            } else {
                return@setOnClickListener
            }

        }
    }
}
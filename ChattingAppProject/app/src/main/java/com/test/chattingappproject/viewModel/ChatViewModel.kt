package com.test.chattingappproject.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.chattingappproject.dataModel.ChatModel
import com.test.chattingappproject.dataModel.ChatRoomModel
import com.test.chattingappproject.dataModel.UserModel
import com.test.chattingappproject.repository.ChattingInfo

class ChatViewModel : ViewModel() {
    //얻어온 채팅방의 채팅 리스트
    private val _chatItem = MutableLiveData<ChatModel>()
    val chatItem: MutableLiveData<ChatModel>
        get() = _chatItem

    fun fetchChattingList(chatRoomId: String) {
        ChattingInfo.getChattingInfo(chatRoomId,
            //통신에 성공했을 경우
            {
                if (it.exists()) {
                    val chatItem = it.getValue(ChatModel::class.java)
                    chatItem ?: return@getChattingInfo
                    _chatItem.value = chatItem
                }
            },
            //통신에 실패했을 경우
            {
                Log.d("getChatList", "${it.toException()}")
            })
    }

}
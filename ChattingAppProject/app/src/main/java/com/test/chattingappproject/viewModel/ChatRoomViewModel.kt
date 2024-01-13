package com.test.chattingappproject.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DatabaseReference
import com.test.chattingappproject.dataModel.ChatRoomModel
import com.test.chattingappproject.repository.ChatRoomInfo

class ChatRoomViewModel : ViewModel() {
    //내가 참여중인 채팅방 리스트
    private val _chatRoomList = MutableLiveData<List<ChatRoomModel>>()
    val chatRoomList: LiveData<List<ChatRoomModel>>
        get() = _chatRoomList

    //내가 참여중인 채팅방 리스트2
    private val _chatRoom = MutableLiveData<ChatRoomModel?>()
    val chatRoom: LiveData<ChatRoomModel?>
        get() = _chatRoom

    fun fetchMyChatRoom(myId: String) {
        ChatRoomInfo.getAllChatRoomInfo(myId,
            //통신에 성공했을 경우
            {
                if (it.exists()) {
                    val chatRoomTemp = mutableListOf<ChatRoomModel>()
                    it.children.map {data ->
                        if (data!=null) chatRoomTemp.add(data.getValue(ChatRoomModel::class.java)!!)
                    }
                    _chatRoomList.value = chatRoomTemp
                }
            },

            //통신에 실패했을 경우
            {
                Log.e("getChatRoomList","${it.toException()}")
            })
    }

    fun fetchOneChatRoom(reference: DatabaseReference){
        ChatRoomInfo.getChatRoomAsReference(reference){
            if (it.value != null){
                _chatRoom.value = it.getValue(ChatRoomModel::class.java)
            }else{
                _chatRoom.value = null
            }
        }
    }


}
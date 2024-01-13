package com.test.chattingappproject.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.chattingappproject.R
import com.test.chattingappproject.dataModel.ChatModel
import com.test.chattingappproject.repository.ChattingInfo
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

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

    fun sendFCM(myUseName:String,newMessage:String,otherUserFcmToken:String){
        val client = OkHttpClient()
        //header 제작
        val root = JSONObject()
        val notification = JSONObject()
        notification.put("title", myUseName)
        notification.put("body", newMessage)

        root.put("to", otherUserFcmToken)
        root.put("priority", "high")
        root.put("notification", notification)

        val requestBody =
            root.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
        val request =
            Request.Builder().post(requestBody).url("https://fcm.googleapis.com/fcm/send")
                .header("Authorization", "key=${R.string.fcm_server_key}").build()
        client.newCall(request).enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {
                Log.e("fcmFunction",e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("fcmFunction",response.message)
            }

        })
    }

}
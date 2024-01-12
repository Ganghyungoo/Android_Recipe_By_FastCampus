package com.test.chattingappproject.dataModel

import android.os.Message

data class ChatRoomModel(
    val chatRoomId: String,
    val otherUserName :String,
    val lastMessage: String

    )
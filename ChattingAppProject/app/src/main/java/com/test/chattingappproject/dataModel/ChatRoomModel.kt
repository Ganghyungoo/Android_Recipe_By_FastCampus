package com.test.chattingappproject.dataModel

import android.os.Message

data class ChatRoomModel(
    val chatRoomId: String? = null,
    val otherUserName :String? = null,
    val lastMessage: String? = null,
    val otherUserId: String? = null
    )
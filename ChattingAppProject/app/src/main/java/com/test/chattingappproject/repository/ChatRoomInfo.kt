package com.test.chattingappproject.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.test.chattingappproject.Key.Companion.DB_CHAT_ROOMS

object ChatRoomInfo {
    fun getAllChatRoomInfo(
        myId:String,
        onChangedFunc: (DataSnapshot) -> Unit,
        onCanceledFunc: (DatabaseError) -> Unit,
    ){
        Firebase.database.reference.child(DB_CHAT_ROOMS).child(myId)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    onChangedFunc(snapshot)
                }

                override fun onCancelled(error: DatabaseError) {
                    onCanceledFunc(error)
                }

            })
    }

    fun getChatRoomAsReference(reference:DatabaseReference,callback:(DataSnapshot)->Unit){
        reference.get().addOnSuccessListener(callback)
    }
}
package com.test.chattingappproject.repository


import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.test.chattingappproject.Key

object ChattingInfo {
    fun getChattingInfo(
        chatRoomId:String,
        onChangedFunc: (DataSnapshot) -> Unit,
        onCanceledFunc: (DatabaseError) -> Unit,
    ){
        Firebase.database.reference.child(Key.DB_CHATS).child(chatRoomId).addChildEventListener(
            object: ChildEventListener{
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    onChangedFunc(snapshot)
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    TODO("Not yet implemented")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    onCanceledFunc(error)
                }

            }
        )
    }
}
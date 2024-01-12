package com.test.chattingappproject.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.test.chattingappproject.Key.Companion.DB_USERS

object UserInfo {
    //나의 정보만 가져오는 메서드
    fun getMyInfo(myDB:DatabaseReference,callBack: (Task<DataSnapshot>)->Unit){
        myDB.get().addOnCompleteListener(callBack)
    }

    //앱의 사용자 정보들을 한번 끌고오는 함수
    fun getAllUserInfo(
        onChangedFunc: (DataSnapshot) -> Unit,
        onCanceledFunc: (DatabaseError) -> Unit,
    ) {
        Firebase.database.reference.child(DB_USERS)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    onChangedFunc(snapshot)
                }

                override fun onCancelled(error: DatabaseError) {
                    onCanceledFunc(error)
                }

            })
    }
}

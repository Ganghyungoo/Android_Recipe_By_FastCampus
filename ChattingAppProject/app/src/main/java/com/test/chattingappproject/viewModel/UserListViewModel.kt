package com.test.chattingappproject.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.chattingappproject.dataModel.UserModel
import com.test.chattingappproject.repository.UserInfo

class UserListViewModel : ViewModel() {
    //앱의 사용자 리스트
    private val _userList = MutableLiveData<List<UserModel>>()
    val userList: LiveData<List<UserModel>>
        get() = _userList

    //유저 정보를 불러오도록 실제로 사용될 메서드
    fun fetchUsers(myId: String) {
        UserInfo.getAllUserInfo(
            //얻어온 결과
            {
                if (it.exists()) {
                    val userTempList = mutableListOf<UserModel>()
                    it.children.forEach { snapshot ->
                        val user = snapshot.getValue(UserModel::class.java)
                        if (user?.userId == myId || user == null){
                            return@forEach
                        }
                        userTempList.add(user)
                    }
                    _userList.value = userTempList
                }
            },
            //결과를 못불러왔을 경우
            {
                Log.e("getUserList","${it.toException()}")
            })
    }
}
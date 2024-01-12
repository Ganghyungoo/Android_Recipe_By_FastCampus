package com.test.chattingappproject.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DatabaseReference
import com.test.chattingappproject.dataModel.UserModel
import com.test.chattingappproject.repository.UserInfo

class MyInfoViewModel : ViewModel() {
    private val _userInfo = MutableLiveData<UserModel>()
    val userInfo: LiveData<UserModel>
        get() = _userInfo

    fun fetchMyInfo(myDB: DatabaseReference) {
        UserInfo.getMyInfo(myDB) {
            if (it.result.exists()) {
                _userInfo.value = it.result.getValue(UserModel::class.java) as UserModel
            }
        }
    }
}

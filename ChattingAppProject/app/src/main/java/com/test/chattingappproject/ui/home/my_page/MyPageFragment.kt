package com.test.chattingappproject.ui.home.my_page

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.test.chattingappproject.Key
import com.test.chattingappproject.R
import com.test.chattingappproject.databinding.FragmentMyPageBinding
import com.test.chattingappproject.ui.sign.LoginActivity
import com.test.chattingappproject.viewModel.MyInfoViewModel


class MyPageFragment : Fragment() {
    private lateinit var fragmentMyPageBinding: FragmentMyPageBinding
    private lateinit var myInfoViewModel: MyInfoViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        fragmentMyPageBinding = FragmentMyPageBinding.inflate(layoutInflater)
        myInfoViewModel = ViewModelProvider(this)[MyInfoViewModel::class.java]

        myInfoViewModel.userInfo.observe(viewLifecycleOwner){
            fragmentMyPageBinding.myIdInputEditText.setText(it.userName.toString())
            fragmentMyPageBinding.myDescriptionTextView.setText(it.description ?: "".toString())
        }

        //나와 관련된 정보 미리 수집
        val myId = Firebase.auth.uid ?: ""
        val myDB = Firebase.database.reference.child(Key.DB_USERS).child(myId)

        //내 정보를 서버에서 가져오는 작업
        myInfoViewModel.fetchMyInfo(myDB)

        fragmentMyPageBinding.updateMyInfoButton.setOnClickListener {
            val myName = fragmentMyPageBinding.myIdInputEditText.text.toString()
            val myDescription = fragmentMyPageBinding.myDescriptionTextView.text.toString()
            if (myName.isEmpty()){
                Toast.makeText(this.context,"유저이름은 빈 값으로 둘 수 없습니다.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //파이어베이스 내 정보 수정
            val user = mutableMapOf<String,Any>()
            user["userName"] = myName
            user["description"] = myDescription
            //수정 완료 시 토스트 메시지
            myDB.updateChildren(user).addOnCompleteListener{
                Toast.makeText(this@MyPageFragment.context,"수정 완료",Toast.LENGTH_SHORT).show()
            }
        }

        fragmentMyPageBinding.logoutButton.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(Intent(this.context,LoginActivity::class.java))
            this.activity?.finish()
        }
        return fragmentMyPageBinding.root
    }
}
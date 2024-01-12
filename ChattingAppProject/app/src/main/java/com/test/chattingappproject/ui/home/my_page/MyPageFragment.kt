package com.test.chattingappproject.ui.home.my_page

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.test.chattingappproject.R
import com.test.chattingappproject.databinding.FragmentMyPageBinding
import com.test.chattingappproject.ui.sign.LoginActivity


class MyPageFragment : Fragment() {
    private lateinit var fragmentMyPageBinding: FragmentMyPageBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        fragmentMyPageBinding = FragmentMyPageBinding.inflate(layoutInflater)

        fragmentMyPageBinding.updateMyInfoButton.setOnClickListener {
            val myName = fragmentMyPageBinding.myIdInputEditText.text.toString()
            val myDescription = fragmentMyPageBinding.myDescriptionTextView.text.toString()
            if (myName.isEmpty()){
                Toast.makeText(this.context,"유저이름은 빈 값으로 둘 수 없습니다.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //TODO 파이어베이스 내 정보 수정
        }

        fragmentMyPageBinding.logoutButton.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(Intent(this.context,LoginActivity::class.java))
            this.activity?.finish()
        }



        return fragmentMyPageBinding.root
    }


}
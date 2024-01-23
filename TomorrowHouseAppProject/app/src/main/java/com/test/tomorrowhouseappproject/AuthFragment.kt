package com.test.tomorrowhouseappproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.test.tomorrowhouseappproject.databinding.FragmentAuthBinding

class AuthFragment : Fragment(R.layout.fragment_auth) {
    private lateinit var fragmentAuthBinding: FragmentAuthBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentAuthBinding = FragmentAuthBinding.inflate(layoutInflater)

        setUpSignInOutButton()
        setUpSignUpButton()

        return fragmentAuthBinding.root
    }

    override fun onStart() {
        super.onStart()
        if (Firebase.auth.currentUser != null){
            initViewToSignInState()
        }else{
            initViewToSignOutState()
        }
    }

    private fun setUpSignUpButton() {
        fragmentAuthBinding.signUpButton.setOnClickListener {
            val email = fragmentAuthBinding.emailEditText.text.trim().toString()
            val password = fragmentAuthBinding.passwordEditText.text.trim().toString()
            if (email.isEmpty() || password.isEmpty()){
                Snackbar.make(fragmentAuthBinding.root,"이메일과 패스워드를 입력하세요", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            Firebase.auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { signUp->
                //만약 회원가입이 성공적이라면
                if (signUp.isSuccessful){
                    Snackbar.make(fragmentAuthBinding.root,"회원가입에 성공했습니다 로그인 해주세요", Snackbar.LENGTH_SHORT).show()
                    fragmentAuthBinding.emailEditText.text.clear()
                    fragmentAuthBinding.passwordEditText.text.clear()
                }else{
                    Snackbar.make(fragmentAuthBinding.root,"회원가입에 실패했습니다", Snackbar.LENGTH_SHORT).show()
                }

            }.addOnFailureListener {
                it.printStackTrace()
            }
        }
    }

    private fun setUpSignInOutButton() {
        fragmentAuthBinding.signInOutButton.setOnClickListener {
            if (Firebase.auth.currentUser == null){
                //로그인
                val email = fragmentAuthBinding.emailEditText.text.trim().toString()
                val password = fragmentAuthBinding.passwordEditText.text.trim().toString()

                Firebase.auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { signIn->
                    //만약 로그인 성공적이라면
                    if (signIn.isSuccessful){
                        initViewToSignInState()
                    }else{
                        Snackbar.make(fragmentAuthBinding.root,"로그인에 실패했습니다 이메일과 패스워드를 확인해주세요",
                            Snackbar.LENGTH_SHORT).show()
                    }

                }

            }else{
                //로그아웃
                Firebase.auth.signOut()
                initViewToSignOutState()
                //Todo 로그인으로 바꾸기 그리고 이메일과 패스워드 텍스트 영역을 활성화 한다 또한 회원가입 버튼도 활성화
            }
        }
    }

    //로그인 버튼을 로그아웃으로 변경하는 메서드(회원가입 버튼 안보이게)
    private fun initViewToSignInState(){
        fragmentAuthBinding.emailEditText.setText(Firebase.auth.currentUser?.email)
        fragmentAuthBinding.emailEditText.isEnabled = false
        fragmentAuthBinding.passwordEditText.visibility = View.GONE
        fragmentAuthBinding.signInOutButton.setText(R.string.signOut)
        fragmentAuthBinding.signUpButton.visibility = View.GONE
    }


    //로그아웃 버튼을 로그인으로 변경하는 메서드(에딧텍스트 영역 비활성화/회원가입 버튼 보이게)
    private fun initViewToSignOutState(){
        fragmentAuthBinding.emailEditText.text.clear()
        fragmentAuthBinding.emailEditText.isEnabled = true
        fragmentAuthBinding.passwordEditText.text.clear()
        fragmentAuthBinding.passwordEditText.visibility = View.VISIBLE
        fragmentAuthBinding.signInOutButton.setText(R.string.signIn)
        fragmentAuthBinding.signUpButton.visibility = View.VISIBLE
    }
}
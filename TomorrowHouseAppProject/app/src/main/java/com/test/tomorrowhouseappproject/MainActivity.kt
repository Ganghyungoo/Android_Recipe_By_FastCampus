package com.test.tomorrowhouseappproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.test.tomorrowhouseappproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)


        setUpSignInOutButton()
        setUpSignUpButton()
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
        activityMainBinding.signUpButton.setOnClickListener {
            val email = activityMainBinding.emailEditText.text.trim().toString()
            val password = activityMainBinding.passwordEditText.text.trim().toString()
            if (email.isEmpty() || password.isEmpty()){
                Snackbar.make(activityMainBinding.root,"이메일과 패스워드를 입력하세요",Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            Firebase.auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { signUp->
                //만약 회원가입이 성공적이라면
                if (signUp.isSuccessful){
                    Snackbar.make(activityMainBinding.root,"회원가입에 성공했습니다 로그인 해주세요",Snackbar.LENGTH_SHORT).show()
                    activityMainBinding.emailEditText.text.clear()
                    activityMainBinding.passwordEditText.text.clear()
                }else{
                    Snackbar.make(activityMainBinding.root,"회원가입에 실패했습니다",Snackbar.LENGTH_SHORT).show()
                }

            }.addOnFailureListener {
                it.printStackTrace()
            }
        }
    }

    private fun setUpSignInOutButton() {
        activityMainBinding.signInOutButton.setOnClickListener {
            if (Firebase.auth.currentUser == null){
                //로그인
                val email = activityMainBinding.emailEditText.text.trim().toString()
                val password = activityMainBinding.passwordEditText.text.trim().toString()

                Firebase.auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { signIn->
                    //만약 로그인 성공적이라면
                    if (signIn.isSuccessful){
                        initViewToSignInState()
                        Snackbar.make(activityMainBinding.root,"로그인에 성공했습니다",Snackbar.LENGTH_SHORT).show()
                    }else{
                        Snackbar.make(activityMainBinding.root,"로그인에 실패했습니다 이메일과 패스워드를 확인해주세요",Snackbar.LENGTH_SHORT).show()
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
        activityMainBinding.emailEditText.setText(Firebase.auth.currentUser?.email)
        activityMainBinding.emailEditText.isEnabled = false
        activityMainBinding.passwordEditText.visibility = View.GONE
        activityMainBinding.signInOutButton.setText(R.string.signOut)
        activityMainBinding.signUpButton.visibility = View.GONE
    }


    //로그아웃 버튼을 로그인으로 변경하는 메서드(에딧텍스트 영역 비활성화/회원가입 버튼 보이게)
    private fun initViewToSignOutState(){
        activityMainBinding.emailEditText.text.clear()
        activityMainBinding.emailEditText.isEnabled = true
        activityMainBinding.passwordEditText.text.clear()
        activityMainBinding.passwordEditText.visibility = View.VISIBLE
        activityMainBinding.signInOutButton.setText(R.string.signIn)
        activityMainBinding.signUpButton.visibility = View.VISIBLE
    }
}
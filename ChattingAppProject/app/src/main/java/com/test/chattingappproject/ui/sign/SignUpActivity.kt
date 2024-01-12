package com.test.chattingappproject.ui.sign

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.test.chattingappproject.R
import com.test.chattingappproject.databinding.ActivitySignUpBinding
import com.test.chattingappproject.ui.MainActivity

class SignUpActivity : AppCompatActivity() {
    lateinit var activitySignUpBinding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySignUpBinding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(activitySignUpBinding.root)

        activitySignUpBinding.signUpButton.setOnClickListener {
            val signIdText = activitySignUpBinding.signIdInputEditText.text.toString()
            val signPwText = activitySignUpBinding.signPwInputEditText.text.toString()
            val signPwCheckText = activitySignUpBinding.signCheckPwInputEditText.text.toString()
            //만약 비어있는 값일 경우
            if(signIdText.isEmpty() || signPwText.isEmpty()){
                if (signIdText.isEmpty()){
                    activitySignUpBinding.signIdInputEditText.error = "아이디를 입력하세요!"
                }
                else{
                    activitySignUpBinding.signPwInputEditText.error = "비밀번호를 입력하세요!"
                }
            }
            else if (signPwText != signPwCheckText){
                activitySignUpBinding.signCheckPwInputEditText.error = "비밀번호 유효성 검사에 실패했습니다!"
            }
            //유효성 검사 통과
            else{
                Firebase.auth.createUserWithEmailAndPassword(signIdText,signPwText)
                    .addOnCompleteListener(this){auth ->
                        if (auth.isSuccessful){
                            //회원가입 성공
                            Toast.makeText(this,"회원가입 성공! 로그인 해주세요",Toast.LENGTH_SHORT).show()
                            finish()
                        }else{
                            Log.e("signFunction","${auth.exception}")
                            Toast.makeText(this,"회원가입에 실패했습니다",Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}
package com.test.chattingappproject.ui.sign

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.test.chattingappproject.Key.Companion.DB_USERS
import com.test.chattingappproject.R
import com.test.chattingappproject.databinding.ActivityLoginBinding
import com.test.chattingappproject.repository.UserInfo
import com.test.chattingappproject.ui.MainActivity

class LoginActivity : AppCompatActivity() {
    lateinit var activityLoginBinding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(activityLoginBinding.root)

        //회원가입 하러가기 버튼
        activityLoginBinding.goSignupButton.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
        }

        //로그인 버튼
        activityLoginBinding.loginButton.setOnClickListener {
            val loginIdText = activityLoginBinding.loginIdInputEditText.text.toString()
            val loginPwText = activityLoginBinding.loginPwInputEditText.text.toString()

            //만약 비어있는 값일 경우
            if (loginIdText.isEmpty() || loginPwText.isEmpty()) {
                if (loginIdText.isEmpty()) {
                    activityLoginBinding.loginIdInputEditText.error = "아이디를 입력하세요!"
                } else {
                    activityLoginBinding.loginPwInputEditText.error = "비밀번호를 입력하세요!"
                }
                return@setOnClickListener
            }
            //그렇지 않을 경우(무결성 통과 상태)
            Firebase.auth.signInWithEmailAndPassword(loginIdText, loginPwText)
                .addOnCompleteListener(this) {
                    val myId = Firebase.auth.uid
                    if (it.isSuccessful && !myId.isNullOrEmpty()) {

                        Firebase.messaging.token.addOnCompleteListener {
                            val token = it.result
                            val updateObj = mutableMapOf<String,Any>()
                            updateObj["userId"] = myId
                            updateObj["userName"] = loginIdText
                            updateObj["fcmToken"] = token

                            Firebase.database.reference.child(DB_USERS).child(myId).updateChildren(updateObj)

                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        }
                    } else {
                        Log.e("sign", "${it.exception}")
                        Toast.makeText(this, "로그인에 실패했습니다", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
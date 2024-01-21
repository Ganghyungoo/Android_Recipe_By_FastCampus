package com.test.zenlyappproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.AccessTokenInfo
import com.kakao.sdk.user.model.User
import com.test.zenlyappproject.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var activityLoginBinding: ActivityLoginBinding
    private lateinit var emailLoginResult: ActivityResultLauncher<Intent>
    private lateinit var pendingUser: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(activityLoginBinding.root)

        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo{ accessTokenInfo: AccessTokenInfo?, throwable: Throwable? ->
                if (throwable == null){
                    if (Firebase.auth.currentUser == null){
                        //카카오톡에서 정보를 끌고 와서 파이어베이스 로그인
                        getKakaoAccountInfo()
                    }else{
                        navigateToMapActivity()
                    }
                }
            }
        }

        emailLoginResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode == RESULT_OK){

                val email = it.data?.getStringExtra("email")
                if (email == null){
                    showErrorToast()
                    return@registerForActivityResult
                }else{
                    signinFirebase(pendingUser, email)
                }
            }
        }

        // 카카오계정으로 로그인 공통 callback 구성
        // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                showErrorToast()
                error.printStackTrace()
                Log.e("testt", "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                getKakaoAccountInfo()
                Log.i("testt", "카카오계정으로 로그인 성공 ${token.accessToken}")
            }
        }

        //로그인 버튼을 클릭했을 경우 카카오 로그인 시도
        activityLoginBinding.loginButton.setOnClickListener {
            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                //카카오톡 로그인
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    if (error != null) {
                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }

                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                        UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                    } else if (token != null) {
                        //카카오 로그인 성공

                        //만약 로그인 한적이 있는지 없는 지 확인
                        if (Firebase.auth.currentUser == null){
                            //카카오톡에서 정보를 끌고 와서 파이어베이스 로그인
                            getKakaoAccountInfo()
                        }else{
                            navigateToMapActivity()
                        }
                        Log.d("testt","로그인 성공 토큰: $token")
                    }
                }
            } else {
                //카카오 계정 로그인
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }
    }

    private fun showErrorToast(){
        Toast.makeText(this, "로그인 실패",Toast.LENGTH_SHORT).show()
    }
    private fun getKakaoAccountInfo() {
        UserApiClient.instance.me { user, error ->
            if (error != null){
                showErrorToast()
                Log.d("testt","getKakaoAccountInfo::  fail:$error")
                error.printStackTrace()
            }else if (user != null){
                //시용자 정보 요청 성공
                Log.d("testt","user: 회원정보 : ${user.id} // 닉네임: ${user.kakaoAccount?.profile?.nickname} // 프로필사진: ${user.kakaoAccount?.profile?.profileImageUrl} ")
                checkKakaoUserData(user)
            }
        }
    }

    private fun checkKakaoUserData(user: User) {
        val kakaoEmail = user.kakaoAccount?.email.orEmpty()

        if (kakaoEmail.isEmpty()){
            //추기로 이메일을 받는 작업
            pendingUser = user
            emailLoginResult.launch(Intent(this,EmailLoginActivity::class.java))
            return
        }
        signinFirebase(user, kakaoEmail)
    }

    private fun signinFirebase(user: User, kakaoEmail: String) {
        val uid = user.id.toString()

        Firebase.auth.createUserWithEmailAndPassword(kakaoEmail,uid).addOnCompleteListener {
            if (it.isSuccessful){
                updateFirebaseDatabase(user)
            }else{
                showErrorToast()
            }
        }.addOnFailureListener {
            //이미 있는 계정인지 확인
            if (it is FirebaseAuthUserCollisionException) {
                Firebase.auth.signInWithEmailAndPassword(kakaoEmail,uid).addOnCompleteListener {result->
                    if (result.isSuccessful){
                        updateFirebaseDatabase(user)
                    }
                }.addOnFailureListener {error->
                    error.printStackTrace()
                    showErrorToast()
                }
            }else{
                showErrorToast()
            }
        }
    }

    private fun updateFirebaseDatabase(user:User){
        val uid = Firebase.auth.currentUser?.uid.orEmpty()
        val personMap = mutableMapOf<String,Any>()
        personMap["uid"] = uid
        personMap["name"] = user.kakaoAccount?.profile?.nickname.orEmpty()
        personMap["profilePhoto"] =  user.kakaoAccount?.profile?.thumbnailImageUrl.orEmpty()

        Firebase.database.reference.child("Person").child(uid).updateChildren(personMap)
        navigateToMapActivity()
    }

    private fun navigateToMapActivity() {
        startActivity(Intent(this,MapActivity::class.java))
    }
}
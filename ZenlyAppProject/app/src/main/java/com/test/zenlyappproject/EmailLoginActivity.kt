package com.test.zenlyappproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.test.zenlyappproject.databinding.ActivityEmailLoginBinding

class EmailLoginActivity : AppCompatActivity() {
    private lateinit var activityEmailLoginBinding: ActivityEmailLoginBinding
    private lateinit var eamilLoginResult: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityEmailLoginBinding = ActivityEmailLoginBinding.inflate(layoutInflater)
        setContentView(activityEmailLoginBinding.root)



        activityEmailLoginBinding.doneButton.setOnClickListener {
            if (activityEmailLoginBinding.emailEditText.text.isNotEmpty()){
                val data = Intent(this,LoginActivity::class.java).apply {
                    putExtra("email",activityEmailLoginBinding.emailEditText.text.toString())
                }
                setResult(RESULT_OK,data)
                finish()
            }else{
                Toast.makeText(this,"빈값을 넣으면 안됩니다",Toast.LENGTH_SHORT).show()
            }
        }



    }
}
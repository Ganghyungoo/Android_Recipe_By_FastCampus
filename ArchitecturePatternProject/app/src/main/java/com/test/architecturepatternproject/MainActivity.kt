package com.test.architecturepatternproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.test.architecturepatternproject.databinding.ActivityMainBinding
import com.test.architecturepatternproject.mvc.MvcActivity
import com.test.architecturepatternproject.mvp.MvpActivity
import com.test.architecturepatternproject.mvvm.MvvmActivity

class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
            it.view = this
        }

    }
    fun openMvc(){
        startActivity(Intent(this@MainActivity,MvcActivity::class.java))
    }
    fun openMvp(){
        startActivity(Intent(this@MainActivity,MvpActivity::class.java))
    }
    fun openMvvm(){
        startActivity(Intent(this@MainActivity,MvvmActivity::class.java))
    }
    fun openMvi(){

    }
}
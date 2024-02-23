package com.test.architecturepatternproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.test.architecturepatternproject.databinding.ActivityMainBinding
import com.test.architecturepatternproject.mvc.MvcActivity

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

    }
    fun openMvvm(){

    }
    fun openMvi(){

    }
}
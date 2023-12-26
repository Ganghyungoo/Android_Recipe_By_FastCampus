package com.test.mediarecoderproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.test.mediarecoderproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var actiivityMainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actiivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(actiivityMainBinding.root)
    }
}
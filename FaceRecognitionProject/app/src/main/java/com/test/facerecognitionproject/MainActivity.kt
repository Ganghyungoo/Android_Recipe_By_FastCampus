package com.test.facerecognitionproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.test.facerecognitionproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        activityMainBinding.startDetectButton.setOnClickListener {
            it.visibility = View.GONE
        }

    }
}
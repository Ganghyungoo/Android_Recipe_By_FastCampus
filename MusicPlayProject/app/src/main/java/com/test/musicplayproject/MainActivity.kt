package com.test.musicplayproject

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.test.musicplayproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var actiivityMainBinding: ActivityMainBinding
    var mediaPlayer:MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actiivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(actiivityMainBinding.root)

        actiivityMainBinding.run {
            buttonStart.setOnClickListener {
                mediaPlayerPlay()
            }
            buttonStop.setOnClickListener {
                mediaPlayerStop()
            }
            buttonPause.setOnClickListener {
                mediaPlayerPause()
            }
        }
    }

    fun mediaPlayerPlay(){
        val intent = Intent(this@MainActivity,MediaPlayerService::class.java).apply {
            action = MEDIA_PLAYER_PLAY
        }
        startService(intent)


    }
    fun mediaPlayerStop(){
        val intent = Intent(this@MainActivity,MediaPlayerService::class.java).apply {
            action = MEDIA_PLAYER_STOP
        }
        startService(intent)
    }
    fun mediaPlayerPause(){
        val intent = Intent(this@MainActivity,MediaPlayerService::class.java).apply {
            action = MEDIA_PLAYER_PAUSE
        }
        startService(intent)
    }
}
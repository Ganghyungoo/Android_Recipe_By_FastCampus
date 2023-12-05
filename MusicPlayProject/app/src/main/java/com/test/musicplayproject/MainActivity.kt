package com.test.musicplayproject

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
        if (mediaPlayer == null){
            mediaPlayer = MediaPlayer.create(this, R.raw.cheer)
        }
        mediaPlayer?.start()
    }
    fun mediaPlayerStop(){
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
    fun mediaPlayerPause(){
        mediaPlayer?.pause()
    }
}
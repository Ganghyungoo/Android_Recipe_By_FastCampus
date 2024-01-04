package com.test.recorderproject

import android.os.Handler
import android.os.Looper

class Timer(listener:OnTimerTickListener) {
    var duration = 0L
    val handler = Handler(Looper.getMainLooper())
    val runnable: Runnable = object : Runnable {
        override fun run() {
            duration += 40L
            handler.postDelayed(this, 40L)
            listener.onTick(duration)
        }
    }

    fun start() {
        handler.postDelayed(runnable, 40L)
    }

    fun stop() {
        handler.removeCallbacks(runnable)
        duration = 0
    }
}

interface OnTimerTickListener {
    fun onTick(duration:Long)
}
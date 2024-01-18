package com.test.yamimapappproject

import android.app.Application
import android.content.Context

class MyApplication :Application(){
    override fun onCreate() {
        super.onCreate()
        MyApplication.applicationContext = this.applicationContext
    }
    companion object{
        lateinit var applicationContext:Context
    }
}
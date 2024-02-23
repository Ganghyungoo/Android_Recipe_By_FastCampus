package com.test.architecturepatternproject.mvp.repository

interface ImageRepository {

    fun getLoadImage(callback: CallBack)

    interface CallBack {
        fun loadImage(url:String, color:String)
    }
}
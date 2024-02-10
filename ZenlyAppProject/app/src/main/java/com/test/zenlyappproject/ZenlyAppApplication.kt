package com.test.zenlyappproject

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class ZenlyAppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Kakao SDK 초기화
        val appKey = getString(R.string.appKey)
        KakaoSdk.init(this, appKey)
    }


}
package com.test.zenlyappproject

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class ZenlyAppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Kakao SDK 초기화
        KakaoSdk.init(this, "068233d2b9991bbee16b747d61a11f0e")
    }


}
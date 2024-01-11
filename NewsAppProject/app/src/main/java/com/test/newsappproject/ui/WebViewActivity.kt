package com.test.newsappproject.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import android.widget.Toast
import com.test.newsappproject.R
import com.test.newsappproject.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {
    private lateinit var activityWebViewBinding: ActivityWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityWebViewBinding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(activityWebViewBinding.root)

        val url = intent.getStringExtra("url")

        activityWebViewBinding.webView.webViewClient = WebViewClient()
        activityWebViewBinding.webView.settings.javaScriptEnabled = true

        if (url.isNullOrEmpty()){
            Toast.makeText(this,"존재하지 않는 뉴스이거나 잘못된 주소 접근입니다",Toast.LENGTH_SHORT).show()
            finish()
        }else{
            activityWebViewBinding.webView.loadUrl(url)
        }

    }
}
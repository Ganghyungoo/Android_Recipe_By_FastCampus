package com.test.webtoonproject

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.content.edit
import com.test.webtoonproject.databinding.FragmentWebViewBinding
import java.text.FieldPosition

class WebViewFragment(val position: Int, val defaultUrl:String) : Fragment() {
    lateinit var fragmentWebViewBinding: FragmentWebViewBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        fragmentWebViewBinding = FragmentWebViewBinding.inflate(layoutInflater)

        fragmentWebViewBinding.run {
            webView.run {
                webViewClient = WebToonWebViewClient(progressBar) { url ->
                    activity?.getSharedPreferences("WEB_HISTORY", Context.MODE_PRIVATE)?.edit {
                        putString("tab$position",url)
                    }
                }
                settings.javaScriptEnabled = true
                loadUrl(defaultUrl)
            }

            buttonBackToLast.setOnClickListener {
                val sharedPreference= activity?.getSharedPreferences("WEB_HISTORY",Context.MODE_PRIVATE)

                val url = sharedPreference?.getString("tab$position","")
                if (url.isNullOrEmpty()){
                    Toast.makeText(context,"마지막 저장 시점이 없습니다",Toast.LENGTH_SHORT).show()
                }else{
                    webView.loadUrl(url)
                }


            }
        }
        return fragmentWebViewBinding.root
    }

    fun canGoBack(): Boolean{
        return fragmentWebViewBinding.webView.canGoBack()
    }

    fun goBack(){
        fragmentWebViewBinding.webView.goBack()
    }
}
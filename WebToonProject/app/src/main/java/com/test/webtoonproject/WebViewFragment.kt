package com.test.webtoonproject

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.edit
import com.test.webtoonproject.databinding.FragmentWebViewBinding

class WebViewFragment(val position: Int, val defaultUrl:String) : Fragment() {
    private lateinit var mainActivity: MainActivity
    private lateinit var fragmentWebViewBinding: FragmentWebViewBinding
    var tabNameChangedListener:TabNameChangedListener? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        fragmentWebViewBinding = FragmentWebViewBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity



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
            buttonChangeTabName.setOnClickListener {
                val builder = AlertDialog.Builder(mainActivity)
                val editText : EditText = EditText(context)
                builder.setTitle("탭이름 변경하기")
                builder.setView(editText)
                builder.setNegativeButton("취소하기"){ dialogInterface: DialogInterface, i: Int ->
                    return@setNegativeButton
                }
                builder.setPositiveButton("변경하기"){ dialogInterface: DialogInterface, i: Int ->
                    activity?.getSharedPreferences("TabName",Context.MODE_PRIVATE)?.edit {
                        putString("tab${position}_name",editText.text.toString())
                        tabNameChangedListener?.tabNameChanged(position,editText.text.toString())
                    }
                }
                builder.show()
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
interface TabNameChangedListener{
    fun tabNameChanged(position:Int, name:String)
}
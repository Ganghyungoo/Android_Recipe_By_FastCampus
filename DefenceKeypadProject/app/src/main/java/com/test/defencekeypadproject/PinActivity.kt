package com.test.defencekeypadproject


import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.test.defencekeypadproject.databinding.ActivityPinBinding
import com.test.defencekeypadproject.widget.ShuffleNumberKeyboard

class PinActivity: AppCompatActivity(),ShuffleNumberKeyboard.KeyPadListener {
    private lateinit var activityPinBinding: ActivityPinBinding
    private val viewModel: PinViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityPinBinding = ActivityPinBinding.inflate(layoutInflater)
        setContentView(activityPinBinding.root)
        activityPinBinding.viewModel = viewModel
        
        //바인딩의 라이브데이터 사용을 위해서 바인딩에 라이프사이클 오너를 연결시킨다
        activityPinBinding.lifecycleOwner = this

        activityPinBinding.shuffleKeyboard.setKeyPadListener(this)

        viewModel.messageLiveData.observe(this){
            Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClickNum(num: String) {
        viewModel.input(num)
    }

    override fun onClickDelete() {
        viewModel.delete()
    }

    override fun onClickDone() {
        viewModel.done()
    }

}
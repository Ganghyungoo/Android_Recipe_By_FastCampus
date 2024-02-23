package com.test.architecturepatternproject.mvvm

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.test.architecturepatternproject.databinding.ActivityMvvmBinding
import com.test.architecturepatternproject.mvvm.repository.ImageRepositoryImpl

class MvvmActivity:AppCompatActivity() {

    private lateinit var activityMvvmBinding: ActivityMvvmBinding

    private val viewModel: MvvmViewModel by viewModels {
        MvvmViewModel.MvvmViewModelFactory(ImageRepositoryImpl())
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMvvmBinding = ActivityMvvmBinding.inflate(layoutInflater).also {
            setContentView(it.root)
            it.lifecycleOwner = this
            it.view = this
            it.viewModel = viewModel

        }
    }
    fun loadImage(){
        viewModel.loadRandomImage()
    }
}
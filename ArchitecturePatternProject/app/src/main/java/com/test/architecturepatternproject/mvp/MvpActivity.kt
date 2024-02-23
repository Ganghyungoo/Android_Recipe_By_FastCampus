package com.test.architecturepatternproject.mvp

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.test.architecturepatternproject.databinding.ActivityMvpBinding
import com.test.architecturepatternproject.mvc.ImageCountModel
import com.test.architecturepatternproject.mvp.repository.ImageRepositoryImpl

class MvpActivity:AppCompatActivity(),MvpContractor.View {

    private lateinit var activityMvpBinding: ActivityMvpBinding

    private lateinit var presenter: MvpContractor.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMvpBinding = ActivityMvpBinding.inflate(layoutInflater).also {
            setContentView(it.root)
            it.view = this@MvpActivity
        }
        presenter = MvpPresenter(ImageCountModel() , ImageRepositoryImpl())
        presenter.attachView(this)

    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    fun loadImage() {
        presenter.loadRandomImage()
    }

    override fun showImage(url: String, color: String) {
        activityMvpBinding.imageView.run {
            setBackgroundColor(Color.parseColor(color))
            load(url){
                crossfade(300)
            }
        }
    }

    override fun showImageCountText(count: Int) {
        activityMvpBinding.imageCountTextView.text = "불러온 이미지 수 : $count"
    }
}
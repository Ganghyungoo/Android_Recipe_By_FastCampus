package com.test.architecturepatternproject.mvc

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.test.architecturepatternproject.databinding.ActivityMvcBinding
import com.test.architecturepatternproject.mvc.provider.ImageProvider

class MvcActivity:AppCompatActivity(), ImageProvider.Callback {

    private lateinit var activityMvcBinding: ActivityMvcBinding
    private val model = ImageCountModel()
    private val imageProvider = ImageProvider(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMvcBinding = ActivityMvcBinding.inflate(layoutInflater)
        setContentView(activityMvcBinding.root)
        activityMvcBinding.view = this@MvcActivity




    }
    fun loadImage() {
        imageProvider.getRandomImage()
    }

    override fun loadImage(url: String, color: String) {
        model.increase()
        with(activityMvcBinding){
            imageView.run {
                setBackgroundColor(Color.parseColor(color))
                load(url)
            }
            imageCountTextView.text = "불러온 이미지 수: ${model.count}"
        }
    }
}
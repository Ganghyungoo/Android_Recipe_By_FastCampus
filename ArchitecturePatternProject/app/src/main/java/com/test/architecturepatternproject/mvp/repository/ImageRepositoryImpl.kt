package com.test.architecturepatternproject.mvp.repository

import com.test.architecturepatternproject.ImageResponse
import com.test.architecturepatternproject.RetrofitManager
import com.test.architecturepatternproject.mvc.provider.ImageProvider
import retrofit2.Call
import retrofit2.Response

class ImageRepositoryImpl: ImageRepository {

    override fun getLoadImage(callback: ImageRepository.CallBack) {
        RetrofitManager.imageService.getRandomImage()
            .enqueue(object: retrofit2.Callback<ImageResponse>{
                override fun onResponse(
                    call: Call<ImageResponse>,
                    response: Response<ImageResponse>,
                ) {
                    if (response.isSuccessful){
                        response.body()?.let {
                            callback.loadImage(it.urls.regular, it.color)
                        }
                    }
                }

                override fun onFailure(call: Call<ImageResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }
}
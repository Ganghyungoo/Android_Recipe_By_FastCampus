package com.test.architecturepatternproject.mvvm.repository

import com.test.architecturepatternproject.mvvm.model.Image
import io.reactivex.Single

interface ImageRepository {

    fun getRandomImage(): Single<Image>
}
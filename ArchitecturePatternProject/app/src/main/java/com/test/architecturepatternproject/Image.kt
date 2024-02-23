package com.test.architecturepatternproject

import android.net.Uri
import com.google.gson.annotations.SerializedName

data class ImageResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("urls")
    val urls: UrlResponse,
    @SerializedName("color")
    val color: String,
)

data class UrlResponse(
    val raw: String,
    val full: String,
    val regular: String,
    val small: String,
    val thumb: String,
)
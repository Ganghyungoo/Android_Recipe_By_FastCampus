package com.test.searchmediaproject.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class VideoResponse(
    val documents: List<VideoItem>
)
data class VideoItem(
    @SerializedName("thumbnail") override val thumbnailUrl: String,
    @SerializedName("title") val title: String,
    @SerializedName("play_time") val playTime: Int,
    @SerializedName("author") val author: String,
    @SerializedName("datetime") override val dateTime: Date,
    override var isFavorite: Boolean
):ListItem
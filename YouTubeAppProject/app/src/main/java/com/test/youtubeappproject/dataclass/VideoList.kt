package com.test.youtubeappproject.dataclass

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class VideoList(
    val videos: List<VideoItem>,
)
data class VideoItem(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("sources")
    val videoUrl: String,
    @SerializedName("channelName")
    val channelName: String,
    @SerializedName("viewCount")
    val viewCount: String,
    @SerializedName("dateText")
    val dateText: String,
    @SerializedName("channelThumb")
    val channelThumb: String,
    @SerializedName("thumb")
    val thumb: String,
)

package com.test.weatherappproject.dataclass

import com.google.gson.annotations.SerializedName

data class ResponseData(
    @SerializedName("response")
    val response: FirstData,
)

data class FirstData(
    @SerializedName("body")
    val body: BodyData,
    @SerializedName("header")
    val header: HeaderData,
)

data class HeaderData(
    @SerializedName("resultCode")
    val resultCode: String,
    @SerializedName("resultMsg")
    val resultMsg: String,
)

data class BodyData(
    @SerializedName("items")
    val items: Items,
)

data class Items(
    @SerializedName("item")
    val item: List<Item>,
)

data class Item(
    @SerializedName("baseDate")
    val baseDate: String,
    @SerializedName("baseTime")
    val baseTime: String,
    @SerializedName("category")
    val category: Category?,
    @SerializedName("fcstDate")
    val fcstDate: String,
    @SerializedName("fcstTime")
    val fcstTime: String,
    @SerializedName("fcstValue")
    val fcstValue: String,
    @SerializedName("nx")
    val nx:String,
    @SerializedName("ny")
    val ny:String
)
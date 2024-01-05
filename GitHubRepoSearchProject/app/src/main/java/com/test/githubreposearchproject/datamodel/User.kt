package com.test.githubreposearchproject.datamodel

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: String,
    @SerializedName("login")
    val userName: String,
)
package com.test.motionanimationproject

data class Home(
    val user: User,
    val appBarImage: String
)

data class User(
    val nickName: String,
    val starCount: Int,
    val totalCount: Int
)
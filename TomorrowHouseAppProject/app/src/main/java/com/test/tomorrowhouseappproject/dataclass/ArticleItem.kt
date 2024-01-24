package com.test.tomorrowhouseappproject.dataclass

data class ArticleItem(
    val articleId: String,
    val createAt: Long,
    val description: String,
    val imageUrl: String,
    var isBookMark : Boolean
)
package com.test.newsappproject.dataModel

data class NewsModel(
    val title: String,
    val link: String,
    var imageUrl:String? = null
)
fun List<NewsItem>.changeToModel(): List<NewsModel>{
    return this.map {
        NewsModel(
            title = it.title ?: "",
            link = it.link ?: "",
            imageUrl = null
        )
    }
}

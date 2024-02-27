package com.test.shoppingmallproject.model

data class Image(
    val imageUrl: String
):ListItem {
    override val viewType: ViewType
        get() = ViewType.IMAGE
}

package com.test.shoppingmallproject.model

data class Coupon(
    val imageUrl: String,
    val name: String,
    val coupon: String,
) : ListItem {
    override val viewType: ViewType
        get() = ViewType.IMAGE
}

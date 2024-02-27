package com.test.shoppingmallproject.model

import android.view.View

interface ListItem:java.io.Serializable{
    val viewType:ViewType

    fun getKey() = hashCode()
}
enum class ViewType{
    VIEW_PAGER,
    HORIZONTAL,
    FULL_AD,

    SELL_ITEM,
    IMAGE,
    SALE,
    COUPON,

    EMPTY
}
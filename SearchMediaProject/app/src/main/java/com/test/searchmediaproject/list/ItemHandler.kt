package com.test.searchmediaproject.list

import com.test.searchmediaproject.model.ListItem

interface ItemHandler {
    fun onClickFavorite(item: ListItem)
}
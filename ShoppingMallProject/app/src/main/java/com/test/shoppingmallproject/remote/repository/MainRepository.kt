package com.test.shoppingmallproject.remote.repository

import androidx.paging.PagingData
import com.test.shoppingmallproject.model.ListItem
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    fun loadList(): Flow<PagingData<ListItem>>
}
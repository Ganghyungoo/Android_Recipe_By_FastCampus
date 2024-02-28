package com.test.shoppingmallproject.remote.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.test.shoppingmallproject.model.ListItem
import com.test.shoppingmallproject.remote.MainPagingSource
import com.test.shoppingmallproject.remote.MainService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val mainService: MainService
): MainRepository {
    override fun loadList(): Flow<PagingData<ListItem>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            MainPagingSource(mainService)
        }
    ).flow
}
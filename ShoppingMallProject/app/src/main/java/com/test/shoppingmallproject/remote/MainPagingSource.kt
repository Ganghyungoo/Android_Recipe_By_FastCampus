package com.test.shoppingmallproject.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.test.shoppingmallproject.model.ListItem
import java.lang.Exception

class MainPagingSource(private val mainService: MainService): PagingSource<Int, ListItem>() {
    override fun getRefreshKey(state: PagingState<Int, ListItem>) = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListItem> {
        return try{
            val page = params.key ?: 1
            val size = params.loadSize
            val result = SampleMock.mockChapter6List()//mainService.getList(page, size).data

            LoadResult.Page(
                data = result,
                prevKey = null,
                nextKey = null/*result.page.nextPage*/
            )
        }catch (e: Exception){
            LoadResult.Error(e)
        }
    }
}
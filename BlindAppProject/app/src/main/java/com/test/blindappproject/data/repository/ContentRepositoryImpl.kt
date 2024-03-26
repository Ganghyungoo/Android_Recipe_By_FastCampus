package com.test.blindappproject.data.repository

import com.test.blindappproject.data.model.ContentMapper.toContent
import com.test.blindappproject.data.model.ContentMapper.toEntity
import com.test.blindappproject.data.model.ContentMapper.toRequest
import com.test.blindappproject.data.source.local.dao.ContentDao
import com.test.blindappproject.data.source.remote.api.ContentService
import com.test.blindappproject.domain.model.Content
import com.test.blindappproject.domain.repository.ContentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class ContentRepositoryImpl @Inject constructor(
    private val contentService: ContentService,
    private val contentDao: ContentDao,
) : ContentRepository {
    override fun loadList(): Flow<List<Content>> {
        return flow {
            /*contentDao.selectAll().collect(){list->
                emit(list.map { it.toContent() })
            }*/
            emit(try {
                contentService.getList().data.map { it.toContent() }
            } catch (e: IOException) {
                emptyList()
            }
            )
        }
    }

    override suspend fun save(item: Content): Boolean {
        return try {
            contentService.saveItem(item.toRequest())
            contentDao.insert(item.toEntity())
            true
        } catch (e: IOException) {
            false
        }
    }

    override suspend fun update(item: Content): Boolean {
        return try {
            contentService.updateItem(item.toRequest())
            contentDao.insert(item.toEntity())
            true
        } catch (e: IOException) {
            false
        }
    }

    override suspend fun delete(item: Content): Boolean {
        return try {
            item.id?.let {id->
                contentService.deleteItem(id)
            }
            contentDao.delete(item.toEntity())
            true
        }catch (e:IOException){
            false
        }
    }
}
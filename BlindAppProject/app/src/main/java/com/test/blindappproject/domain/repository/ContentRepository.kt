package com.test.blindappproject.domain.repository

import com.test.blindappproject.domain.model.Content
import kotlinx.coroutines.flow.Flow

interface ContentRepository {

    fun loadList(): Flow<List<Content>>

    suspend fun save(item: Content): Boolean

    suspend fun update(item: Content): Boolean

    suspend fun delete(item: Content): Boolean
}
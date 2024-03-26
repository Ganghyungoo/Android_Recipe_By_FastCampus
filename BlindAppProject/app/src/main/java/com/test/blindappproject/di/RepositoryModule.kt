package com.test.blindappproject.di

import com.test.blindappproject.data.repository.ContentRepositoryImpl
import com.test.blindappproject.data.source.local.dao.ContentDao
import com.test.blindappproject.data.source.remote.api.ContentService
import com.test.blindappproject.domain.repository.ContentRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun providesContentRepository(
        contentService: ContentService,
        contentDao: ContentDao
    ):ContentRepository = ContentRepositoryImpl(contentService, contentDao)
}
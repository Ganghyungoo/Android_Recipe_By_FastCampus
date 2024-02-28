package com.test.shoppingmallproject.di

import com.test.shoppingmallproject.remote.MainService
import com.test.shoppingmallproject.remote.repository.MainRepository
import com.test.shoppingmallproject.remote.repository.MainRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object MainRepositoryModule {

    @ViewModelScoped
    @Provides
    fun providesMainRepository(
        mainService: MainService
    ): MainRepository = MainRepositoryImpl(mainService)
}
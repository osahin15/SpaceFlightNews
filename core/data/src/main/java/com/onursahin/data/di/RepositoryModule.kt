package com.onursahin.data.di

import com.onursahin.data.repository.FavoriteNewsRepositoryImpl
import com.onursahin.data.repository.SpaceNewsRepositoryImpl
import com.onursahin.domain.repository.FavoriteNewsRepository
import com.onursahin.domain.repository.SpaceNewsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindSpaceNewsRepository(
        impl: SpaceNewsRepositoryImpl
    ): SpaceNewsRepository

    @Binds
    abstract fun bindFavoriteNewsRepository(
        impl: FavoriteNewsRepositoryImpl
    ): FavoriteNewsRepository

}
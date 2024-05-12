package com.tinderlikeapp.data.di

import com.tinderlikeapp.data.network.repository.CharactersRepository
import com.tinderlikeapp.data.network.repository.CharactersRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindCharacterRepository(
        charactersRepository: CharactersRepositoryImpl
    ): CharactersRepository

}
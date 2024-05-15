package com.tinderlikeapp.di

import android.app.Application
import com.tinderlikeapp.application.TimberLikeAppApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApp(
        application: Application
    ) = application as TimberLikeAppApplication

}
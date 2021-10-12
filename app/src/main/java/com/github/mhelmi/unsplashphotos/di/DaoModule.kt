package com.github.mhelmi.unsplashphotos.di

import com.github.mhelmi.unsplashphotos.common.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {
  @Provides
  @Singleton
  fun providePhotosDao(database: AppDatabase) = database.getPhotosDao()
}
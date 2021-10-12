package com.github.mhelmi.unsplashphotos.di

import com.github.mhelmi.unsplashphotos.data.photos.PhotosRepositoryImpl
import com.github.mhelmi.unsplashphotos.domain.photos.PhotosRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoriesModule {
  @Binds
  abstract fun bindPhotosRepository(photosRepository: PhotosRepositoryImpl): PhotosRepository
}
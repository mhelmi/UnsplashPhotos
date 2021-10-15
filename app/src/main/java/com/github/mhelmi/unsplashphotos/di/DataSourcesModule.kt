package com.github.mhelmi.unsplashphotos.di

import com.github.mhelmi.unsplashphotos.data.photos.sources.PhotosLocalSource
import com.github.mhelmi.unsplashphotos.data.photos.sources.PhotosRemoteSource
import com.github.mhelmi.unsplashphotos.data.photos.sources.local.PhotosLocalSourceImpl
import com.github.mhelmi.unsplashphotos.data.photos.sources.remote.PhotosRemoteSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourcesModule {
    @Binds
    abstract fun bindPhotosLocalSource(photosLocalSource: PhotosLocalSourceImpl): PhotosLocalSource

    @Binds
    abstract fun bindPhotosRemoteSource(photosRemoteSource: PhotosRemoteSourceImpl): PhotosRemoteSource
}
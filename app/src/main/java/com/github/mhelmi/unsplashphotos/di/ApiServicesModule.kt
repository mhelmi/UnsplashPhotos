package com.github.mhelmi.unsplashphotos.di

import com.github.mhelmi.unsplashphotos.data.photos.sources.remote.PhotosApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiServicesModule {

    @Provides
    @Singleton
    fun providePhotosApiService(@MainClient retrofit: Retrofit): PhotosApiService {
        return retrofit.create(PhotosApiService::class.java)
    }
}
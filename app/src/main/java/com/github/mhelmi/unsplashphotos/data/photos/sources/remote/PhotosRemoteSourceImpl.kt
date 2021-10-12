package com.github.mhelmi.unsplashphotos.data.photos.sources.remote

import com.github.mhelmi.unsplashphotos.data.photos.sources.PhotosRemoteSource
import com.github.mhelmi.unsplashphotos.data.photos.sources.remote.model.PhotoRemote
import com.github.mhelmi.unsplashphotos.di.IoDispatcher
import com.github.mhelmi.unsplashphotos.domain.photos.model.Photo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PhotosRemoteSourceImpl @Inject constructor(
    private val photosApiService: PhotosApiService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : PhotosRemoteSource {

    override fun getPhotosList(page: Int, pageSize: Int): Flow<List<PhotoRemote>> = flow {
        emit(photosApiService.getPhotosList(page, pageSize))
    }.flowOn(ioDispatcher)
}
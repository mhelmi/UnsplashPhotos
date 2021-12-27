package com.github.mhelmi.unsplashphotos.data.photos.sources.remote

import com.github.mhelmi.unsplashphotos.data.photos.sources.PhotosRemoteSource
import com.github.mhelmi.unsplashphotos.data.photos.sources.remote.model.PhotoRemote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PhotosRemoteSourceImpl @Inject constructor(
  private val photosApiService: PhotosApiService,
) : PhotosRemoteSource {

  override fun getPhotosList(page: Int, pageSize: Int): Flow<List<PhotoRemote>> = flow {
    emit(photosApiService.getPhotosList(page, pageSize))
  }
}
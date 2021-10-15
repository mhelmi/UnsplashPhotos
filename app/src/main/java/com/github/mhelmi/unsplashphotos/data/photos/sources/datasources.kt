package com.github.mhelmi.unsplashphotos.data.photos.sources

import com.github.mhelmi.unsplashphotos.data.photos.sources.local.model.PhotoEntity
import com.github.mhelmi.unsplashphotos.data.photos.sources.remote.model.PhotoRemote
import kotlinx.coroutines.flow.Flow

interface PhotosLocalSource {
  fun getPhotosList(): Flow<List<PhotoEntity>>

  suspend fun updatePhotosList(list: List<PhotoEntity>)
}

interface PhotosRemoteSource {
  fun getPhotosList(page: Int, pageSize: Int): Flow<List<PhotoRemote>>
}
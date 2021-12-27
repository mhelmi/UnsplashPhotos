package com.github.mhelmi.unsplashphotos.data.photos.sources.local

import com.github.mhelmi.unsplashphotos.data.photos.sources.PhotosLocalSource
import com.github.mhelmi.unsplashphotos.data.photos.sources.local.dao.PhotosDao
import com.github.mhelmi.unsplashphotos.data.photos.sources.local.model.PhotoEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PhotosLocalSourceImpl @Inject constructor(
  private val photosDao: PhotosDao,
) : PhotosLocalSource {

  override fun getPhotosList(): Flow<List<PhotoEntity>> = flow {
    emitAll(photosDao.getPhotosList())
  }

  override suspend fun updatePhotosList(list: List<PhotoEntity>) {
    photosDao.updatePhotos(list)
  }
}
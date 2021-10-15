package com.github.mhelmi.unsplashphotos.data.photos.sources.local

import com.github.mhelmi.unsplashphotos.data.photos.sources.PhotosLocalSource
import com.github.mhelmi.unsplashphotos.data.photos.sources.local.dao.PhotosDao
import com.github.mhelmi.unsplashphotos.data.photos.sources.local.model.PhotoEntity
import com.github.mhelmi.unsplashphotos.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PhotosLocalSourceImpl @Inject constructor(
  private val photosDao: PhotosDao,
  @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : PhotosLocalSource {

  override fun getPhotosList(): Flow<List<PhotoEntity>> = flow {
    emitAll(photosDao.getPhotosList())
  }.flowOn(ioDispatcher)

  override suspend fun updatePhotosList(list: List<PhotoEntity>) {
    photosDao.updatePhotos(list)
  }
}
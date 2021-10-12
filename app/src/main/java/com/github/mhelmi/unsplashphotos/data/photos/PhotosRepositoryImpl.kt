package com.github.mhelmi.unsplashphotos.data.photos

import com.github.mhelmi.unsplashphotos.data.photos.sources.PhotosLocalSource
import com.github.mhelmi.unsplashphotos.data.photos.sources.PhotosRemoteSource
import com.github.mhelmi.unsplashphotos.data.photos.sources.mapper.toPhotoEntityList
import com.github.mhelmi.unsplashphotos.data.photos.sources.mapper.toPhotoList
import com.github.mhelmi.unsplashphotos.domain.photos.PhotosRepository
import com.github.mhelmi.unsplashphotos.domain.photos.model.Photo
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class PhotosRepositoryImpl @Inject constructor(
  private val localSource: PhotosLocalSource,
  private val remoteSource: PhotosRemoteSource
) : PhotosRepository {
  @FlowPreview
  override fun getPhotosList(page: Int, pageSize: Int): Flow<List<Photo>> = flow {
    emitAll(localSource.getPhotosList().map { it.toPhotoList() })
    remoteSource.getPhotosList(page, pageSize).collect {
      localSource.updatePhotosList(it.toPhotoEntityList())
    }
  }
}
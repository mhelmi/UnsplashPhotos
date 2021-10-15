package com.github.mhelmi.unsplashphotos.data.photos

import com.github.mhelmi.unsplashphotos.data.photos.sources.PhotosLocalSource
import com.github.mhelmi.unsplashphotos.data.photos.sources.PhotosRemoteSource
import com.github.mhelmi.unsplashphotos.data.photos.sources.mapper.toPhotoEntityList
import com.github.mhelmi.unsplashphotos.data.photos.sources.mapper.toPhotoList
import com.github.mhelmi.unsplashphotos.domain.photos.PhotosRepository
import com.github.mhelmi.unsplashphotos.domain.photos.model.Photo
import com.github.mhelmi.unsplashphotos.domain.photos.model.PhotosConst
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

class PhotosRepositoryImpl @Inject constructor(
  private val localSource: PhotosLocalSource,
  private val remoteSource: PhotosRemoteSource
) : PhotosRepository {

  @FlowPreview
  override fun getPhotosList(page: Int, pageSize: Int): Flow<List<Photo>> {
    return remoteSource.getPhotosList(page, pageSize)
      .onEach {
        if (page == PhotosConst.START_PAGE) {
          localSource.updatePhotosList(it.toPhotoEntityList())
        }
      }
      .map { it.toPhotoList() }
      .catch { t ->
        Timber.e(t)
        if (page == PhotosConst.START_PAGE) {
          emitAll(localSource.getPhotosList().map { it.toPhotoList() })
        } else {
          emit(emptyList())
        }
      }
  }
}
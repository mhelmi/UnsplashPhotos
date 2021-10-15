package com.github.mhelmi.unsplashphotos.domain.photos.usecase

import com.github.mhelmi.unsplashphotos.domain.photos.PhotosRepository
import javax.inject.Inject

class GetPhotosUseCase @Inject constructor(
  private val photosRepository: PhotosRepository
) {

  operator fun invoke(page: Int, pageSize: Int) = photosRepository.getPhotosList(page, pageSize)
}
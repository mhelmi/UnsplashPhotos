package com.github.mhelmi.unsplashphotos.domain.photos.usecase

import com.github.mhelmi.unsplashphotos.domain.photos.PhotosRepository
import com.github.mhelmi.unsplashphotos.domain.photos.model.Photo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPhotosUseCase @Inject constructor(
    private val photosRepository: PhotosRepository
) {

    operator fun invoke(page: Int, pageSize: Int): Flow<List<Photo>> {
        TODO()
    }
}
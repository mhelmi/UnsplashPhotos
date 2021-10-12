package com.github.mhelmi.unsplashphotos.domain.photos

import com.github.mhelmi.unsplashphotos.domain.photos.model.Photo
import kotlinx.coroutines.flow.Flow

interface PhotosRepository {
    fun getPhotosList(page: Int, pageSize: Int): Flow<List<Photo>>
}
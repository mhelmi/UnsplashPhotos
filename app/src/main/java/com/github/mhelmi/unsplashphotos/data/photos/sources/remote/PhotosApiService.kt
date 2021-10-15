package com.github.mhelmi.unsplashphotos.data.photos.sources.remote

import com.github.mhelmi.unsplashphotos.data.photos.sources.remote.model.PhotoRemote
import com.github.mhelmi.unsplashphotos.domain.photos.model.Photo
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotosApiService {
    @GET("v2/list")
    fun getPhotosList(
        @Query("page") page: Int,
        @Query("limit") pageSize: Int,
    ): List<PhotoRemote>
}
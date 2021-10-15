package com.github.mhelmi.unsplashphotos.data.photos.sources.mapper

import com.github.mhelmi.unsplashphotos.data.photos.sources.local.model.PhotoEntity
import com.github.mhelmi.unsplashphotos.data.photos.sources.remote.model.PhotoRemote
import com.github.mhelmi.unsplashphotos.domain.photos.model.Photo

fun PhotoEntity.toPhoto(): Photo = Photo(
  id = id,
  author = author,
  photoUrl = photoUrl,
)

@JvmName("mapEntityListToPhotoList")
fun List<PhotoEntity>.toPhotoList() = map { it.toPhoto() }

fun PhotoRemote.toPhoto() = Photo(
  id = id.orEmpty(),
  author = author.orEmpty(),
  photoUrl = downloadUrl.orEmpty(),
)

@JvmName("mapRemoteListToPhotoList")
fun List<PhotoRemote>.toPhotoList() = map { it.toPhoto() }


fun PhotoRemote.toPhotoEntity() = PhotoEntity(
  id = id.orEmpty(),
  author = author.orEmpty(),
  photoUrl = downloadUrl.orEmpty(),
)

fun List<PhotoRemote>.toPhotoEntityList() = map { it.toPhotoEntity() }
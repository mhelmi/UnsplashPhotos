package com.github.mhelmi.unsplashphotos.data.photos.sources.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class PhotoEntity(
  @PrimaryKey val id: String,
  val author: String,
  val photoUrl: String
)
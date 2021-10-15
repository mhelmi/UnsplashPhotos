package com.github.mhelmi.unsplashphotos.common.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.mhelmi.unsplashphotos.data.photos.sources.local.dao.PhotosDao
import com.github.mhelmi.unsplashphotos.data.photos.sources.local.model.PhotoEntity

@Database(
  version = 1,
  exportSchema = false,
  entities = [PhotoEntity::class]
)
@TypeConverters()
abstract class AppDatabase : RoomDatabase() {

  abstract fun getPhotosDao(): PhotosDao

  companion object {
    const val DATA_BASE_NAME = "unsplash_database"
  }
}
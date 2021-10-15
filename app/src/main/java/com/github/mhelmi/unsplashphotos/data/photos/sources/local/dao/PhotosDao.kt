package com.github.mhelmi.unsplashphotos.data.photos.sources.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.mhelmi.unsplashphotos.data.photos.sources.local.model.PhotoEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PhotosDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  abstract suspend fun updatePhotos(list: List<PhotoEntity>)

  @Query("SELECT * FROM photos")
  abstract fun getPhotosList(): Flow<List<PhotoEntity>>
}
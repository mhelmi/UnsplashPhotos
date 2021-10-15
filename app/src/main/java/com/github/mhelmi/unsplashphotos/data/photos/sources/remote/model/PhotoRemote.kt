package com.github.mhelmi.unsplashphotos.data.photos.sources.remote.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class PhotoRemote(
  @SerializedName("id") val id: String? = null,
  @SerializedName("author") val author: String? = null,
  @SerializedName("width") val width: Int? = null,
  @SerializedName("height") val height: Int? = null,
  @SerializedName("url") val url: String? = null,
  @SerializedName("download_url") val downloadUrl: String? = null
) : Parcelable
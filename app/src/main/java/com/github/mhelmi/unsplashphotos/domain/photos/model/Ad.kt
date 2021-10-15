package com.github.mhelmi.unsplashphotos.domain.photos.model

import androidx.annotation.DrawableRes

data class Ad(
  @DrawableRes val imageRes: Int,
  val url: String
)
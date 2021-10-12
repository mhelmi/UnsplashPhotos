package com.github.mhelmi.unsplashphotos.ui.photos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mhelmi.unsplashphotos.domain.photos.usecase.GetPhotosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel @Inject constructor(
  private val getPhotosUseCase: GetPhotosUseCase
) : ViewModel() {
  init {

  }
}
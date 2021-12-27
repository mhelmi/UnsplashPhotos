package com.github.mhelmi.unsplashphotos.ui.photos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mhelmi.unsplashphotos.R
import com.github.mhelmi.unsplashphotos.common.state.UiState
import com.github.mhelmi.unsplashphotos.di.IoDispatcher
import com.github.mhelmi.unsplashphotos.domain.photos.model.Ad
import com.github.mhelmi.unsplashphotos.domain.photos.model.Photo
import com.github.mhelmi.unsplashphotos.domain.photos.model.PhotosConst
import com.github.mhelmi.unsplashphotos.domain.photos.usecase.GetPhotosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel @Inject constructor(
  private val getPhotosUseCase: GetPhotosUseCase,
  @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

  private val _photoListState = MutableStateFlow<UiState<List<Any>>>(UiState.Init())
  val photoListState: StateFlow<UiState<List<Any>>> = _photoListState

  private val _events = Channel<PhotosEvent>()
  val events = _events.receiveAsFlow()

  // pagination properties
  private var nextPage = PhotosConst.START_PAGE
  private var photosList: MutableList<Photo> = mutableListOf()

  val isLoading: Boolean
    get() = _photoListState.value is UiState.Loading || _photoListState.value is UiState.LoadMore

  var isLastPage: Boolean = false

  init {
    loadPhotos()
  }

  fun loadPhotos(page: Int = nextPage, pageSize: Int = PhotosConst.PAGE_SIZE) {
    getPhotosUseCase(page, pageSize)
      .onStart {
        _photoListState.value =
          if (page == PhotosConst.START_PAGE) UiState.Loading() else UiState.LoadMore()
      }
      .map {
        if (page != PhotosConst.START_PAGE && it.isEmpty()) isLastPage = true
        nextPage++
        photosList.addAll(it)
        val photosWithAdsList = insertAdAfterEveryFivePhotos(photosList.toList())
        if (photosWithAdsList.isEmpty()) UiState.Empty() else UiState.Success(photosWithAdsList)
      }
      .onEach { _photoListState.value = it }
      .catch {
        Timber.e(it)
        _photoListState.value = if (photosList.isEmpty()) {
          UiState.Error(it.message)
        } else {
          UiState.SoftError(it.message)
        }
      }
      .flowOn(ioDispatcher)
      .launchIn(viewModelScope)
  }

  fun resetList() {
    nextPage = PhotosConst.START_PAGE
    photosList.clear()
  }

  private fun insertAdAfterEveryFivePhotos(photos: List<Photo>): List<Any> {
    if (photos.size < 5) return photos
    val list: MutableList<Any> = photos.toMutableList()
    val adsCount: Int = photos.size / 5
    var lastAdIndex = 5
    for (i in 1..adsCount) {
      list.add(lastAdIndex, getFixedAd())
      lastAdIndex += 6
    }
    return list.toList()
  }

  private fun getFixedAd() = Ad(R.drawable.ic_fixed_advertisement, "")

  fun onPhotoClicked(photo: Photo) {
    PhotosEvent.OpenPhotoDetails(photo).emit()
  }

  private fun PhotosEvent.emit() = viewModelScope.launch {
    _events.send(this@emit)
  }

  sealed class PhotosEvent {
    data class OpenPhotoDetails(val photo: Photo) : PhotosEvent()
  }
}
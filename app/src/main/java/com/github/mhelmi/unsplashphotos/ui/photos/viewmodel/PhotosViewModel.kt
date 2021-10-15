package com.github.mhelmi.unsplashphotos.ui.photos.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mhelmi.unsplashphotos.R
import com.github.mhelmi.unsplashphotos.common.state.UiState
import com.github.mhelmi.unsplashphotos.di.MainDispatcher
import com.github.mhelmi.unsplashphotos.domain.photos.model.Ad
import com.github.mhelmi.unsplashphotos.domain.photos.model.Photo
import com.github.mhelmi.unsplashphotos.domain.photos.model.PhotosConst
import com.github.mhelmi.unsplashphotos.domain.photos.usecase.GetPhotosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel @Inject constructor(
  private val getPhotosUseCase: GetPhotosUseCase,
  @MainDispatcher private val mainDispatcher: CoroutineDispatcher
) : ViewModel() {

  private val _photoListState = MutableLiveData<UiState<List<Any>>>(UiState.Init())
  val photoListState: LiveData<UiState<List<Any>>> = _photoListState

  private var nextPage = PhotosConst.START_PAGE
  private var photosList: MutableList<Photo> = mutableListOf()

  val isLoading: Boolean
    get() = _photoListState.value is UiState.Loading || _photoListState.value is UiState.LoadMore

  var isLastPage: Boolean = false

  init {
    loadPhotos(nextPage)
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
        photosList.toList()
      }
      .map {
        if (it.isEmpty()) UiState.Empty() else UiState.Success(insertAdAfterEveryFivePhotos(it))
      }
      .onEach { _photoListState.value = it }
      .catch {
        Timber.e(it)
        if (photosList.isEmpty()) {
          _photoListState.value = UiState.Error(it.message)
        } else {
          _photoListState.value = UiState.SoftError(it.message)
        }
      }
      .flowOn(mainDispatcher)
      .launchIn(viewModelScope)
  }

  fun resetList() {
    nextPage = PhotosConst.START_PAGE
    photosList.clear()
  }

  private fun insertAdAfterEveryFivePhotos(photos: List<Photo>): List<Any> {
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
}
package com.github.mhelmi.unsplashphotos.common.state

import androidx.annotation.Keep
import androidx.annotation.StringRes

@Keep
sealed class UiState<T> {

  class Init<T> : UiState<T>()

  /**
   * Show normal loading progress bar.
   */
  class Loading<T> : UiState<T>()

  data class Success<T>(val data: T) : UiState<T>()

  data class Error<T>(val message: String?) : UiState<T>()

  data class SoftError<T>(val message: String?) : UiState<T>()

  class Empty<T>(@StringRes val msgResId: Int = 0) : UiState<T>()

  /**
   * Mainly used in endless scroller of the recycler view to show bottom loading item in the list.
   */
  class LoadMore<T> : UiState<T>()
}
package com.github.mhelmi.unsplashphotos.common.views

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * Created by Muhammad Helmi
 * m.helmi.khalil@gmail.com
 */
abstract class EndlessScrollListener(
  private val layoutManager: RecyclerView.LayoutManager,
  private val pageSize: Int,
) : RecyclerView.OnScrollListener() {

  private val firstVisibleItemPositionFinder: () -> Int = when (layoutManager) {
    is GridLayoutManager -> layoutManager::findFirstVisibleItemPosition
    is StaggeredGridLayoutManager -> { ->
      layoutManager.findFirstVisibleItemPositions(null).run {
        sort()
        this[0]
      }
    }
    is LinearLayoutManager -> layoutManager::findLastVisibleItemPosition
    else -> { -> 0 }
  }

  override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
    super.onScrolled(recyclerView, dx, dy)

    val visibleItemCount = layoutManager.childCount
    val totalItemCount = layoutManager.itemCount
    val firstVisibleItemPosition = firstVisibleItemPositionFinder()

    if (!isLoading() && !isLastPage()) {
      if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
        && firstVisibleItemPosition >= 0
        && totalItemCount >= pageSize
      ) {
        onLoadMore()
      }
    }
  }

  abstract fun onLoadMore()

  abstract fun isLastPage(): Boolean

  abstract fun isLoading(): Boolean

}
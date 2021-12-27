package com.github.mhelmi.unsplashphotos.ui.photos.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mhelmi.unsplashphotos.common.state.UiState
import com.github.mhelmi.unsplashphotos.common.views.EndlessScrollListener
import com.github.mhelmi.unsplashphotos.databinding.FragmentPhotoListBinding
import com.github.mhelmi.unsplashphotos.domain.photos.model.Ad
import com.github.mhelmi.unsplashphotos.domain.photos.model.Photo
import com.github.mhelmi.unsplashphotos.domain.photos.model.PhotosConst
import com.github.mhelmi.unsplashphotos.ui.photos.viewmodel.PhotosViewModel
import com.github.mhelmi.unsplashphotos.ui.photos.viewmodel.PhotosViewModel.PhotosEvent.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PhotoListFragment : Fragment(), PhotosAdapter.PhotosClickListener {

  private var binding: FragmentPhotoListBinding? = null
  private val navController: NavController by lazy { findNavController() }
  private val viewModel by viewModels<PhotosViewModel>()
  private lateinit var photosAdapter: PhotosAdapter
  private lateinit var linearLayoutManager: LinearLayoutManager

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = FragmentPhotoListBinding.inflate(inflater, container, false)
    return binding?.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setClickListeners()
    setupPhotosRecyclerView()
    observeState()
  }

  private fun setClickListeners() = binding?.apply {
    btnTryAgain.setOnClickListener {
      viewModel.apply {
        resetList()
        loadPhotos()
      }
    }
  }

  private fun setupPhotosRecyclerView() = binding?.rvPhotos?.apply {
    layoutManager = LinearLayoutManager(context).also { linearLayoutManager = it }
    setHasFixedSize(true)
    photosAdapter = PhotosAdapter(this@PhotoListFragment)
    adapter = photosAdapter

    addOnScrollListener(object : EndlessScrollListener(linearLayoutManager, PhotosConst.PAGE_SIZE) {
      override fun onLoadMore() = viewModel.loadPhotos()
      override fun isLastPage(): Boolean = viewModel.isLastPage
      override fun isLoading(): Boolean = viewModel.isLoading
    })
  }

  /**
   * Check this link to understand why we need repeatOnLifecycle function and how to use it.
   * https://medium.com/androiddevelopers/a-safer-way-to-collect-flows-from-android-uis-23080b1f8bda
   */
  private fun observeState() {
    with(viewModel) {
      viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
          launch { photoListState.collect(::handlePhotosUiState) }
          launch { events.collect(::handlePhotoEvents) }
        }
      }
    }
  }

  private fun handlePhotosUiState(state: UiState<List<Any>>) {
    showLoading(state is UiState.Loading || state is UiState.LoadMore)
    showEmptyView(state is UiState.Empty)
    when (state) {
      is UiState.Success -> photosAdapter.submitList(state.data)
      else -> Unit
    }
  }

  private fun handlePhotoEvents(event: PhotosViewModel.PhotosEvent) {
    when (event) {
      is OpenPhotoDetails -> navController.navigate(
        PhotoListFragmentDirections.actionPhotoListFragmentToPhotoDetailsFragment(event.photo)
      )
    }
  }

  private fun showLoading(isLoading: Boolean) = binding?.progressBarLoading?.apply {
    isVisible = isLoading
  }

  private fun showEmptyView(isVisible: Boolean) = binding?.layoutEmptyView?.apply {
    this.isVisible = isVisible
  }

  override fun onPhotoClicked(position: Int, photo: Photo) {
    viewModel.onPhotoClicked(photo)
  }

  override fun onAdClick(position: Int, ad: Ad) {
    Toast.makeText(binding?.root?.context, "This is an Advertisement!", Toast.LENGTH_SHORT).show()
  }

  override fun onDestroyView() {
    binding = null
    super.onDestroyView()
  }
}
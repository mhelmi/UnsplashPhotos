package com.github.mhelmi.unsplashphotos.ui.photos.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
import dagger.hilt.android.AndroidEntryPoint

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

  private fun observeState() {
    viewModel.photoListState.observe(viewLifecycleOwner) {
      showLoading(it is UiState.Loading || it is UiState.LoadMore)
      showEmptyView(it is UiState.Empty)
      when (it) {
        is UiState.Success -> photosAdapter.submitList(it.data)
        else -> Unit
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

  private fun showLoading(isLoading: Boolean) = binding?.progressBarLoading?.apply {
    isVisible = isLoading
  }

  private fun showEmptyView(isVisible: Boolean) = binding?.layoutEmptyView?.apply {
    this.isVisible = isVisible
  }

  override fun onPhotoClick(position: Int, photo: Photo) {
    navController.navigate(
      PhotoListFragmentDirections.actionPhotoListFragmentToPhotoDetailsFragment(
        photo
      )
    )
  }

  override fun onAdClick(position: Int, ad: Ad) {
    Toast.makeText(binding?.root?.context, "This is an Advertisement!", Toast.LENGTH_SHORT).show()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    binding = null
  }
}
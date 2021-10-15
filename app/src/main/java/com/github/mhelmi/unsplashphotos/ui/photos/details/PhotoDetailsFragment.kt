package com.github.mhelmi.unsplashphotos.ui.photos.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.mhelmi.unsplashphotos.databinding.FragmentPhotoDetailsBinding
import com.github.mhelmi.unsplashphotos.ui.photos.viewmodel.PhotosViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotoDetailsFragment : Fragment() {

  private var binding: FragmentPhotoDetailsBinding? = null
  private val viewModel by viewModels<PhotosViewModel>()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = FragmentPhotoDetailsBinding.inflate(inflater, container, false)
    return binding?.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

  }

  override fun onDestroyView() {
    super.onDestroyView()
    binding = null
  }
}
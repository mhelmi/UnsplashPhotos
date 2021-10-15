package com.github.mhelmi.unsplashphotos.ui.photos.details

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.github.mhelmi.unsplashphotos.R
import com.github.mhelmi.unsplashphotos.databinding.FragmentPhotoDetailsBinding
import com.github.mhelmi.unsplashphotos.domain.photos.model.Photo
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PhotoDetailsFragment : Fragment() {

  private var binding: FragmentPhotoDetailsBinding? = null
  val args by navArgs<PhotoDetailsFragmentArgs>()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = FragmentPhotoDetailsBinding.inflate(inflater, container, false)
    return binding?.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    feedPhotoDetails(args.photo)
  }

  private fun feedPhotoDetails(photo: Photo) = binding?.apply {
    Glide.with(root)
      .asBitmap()
      .load(photo.photoUrl)
      .listener(object : RequestListener<Bitmap> {
        override fun onLoadFailed(
          e: GlideException?,
          model: Any?,
          target: Target<Bitmap>?,
          isFirstResource: Boolean
        ): Boolean {
          return false
        }

        override fun onResourceReady(
          resource: Bitmap?,
          model: Any?,
          target: Target<Bitmap>?,
          dataSource: DataSource?,
          isFirstResource: Boolean
        ): Boolean {
          ivPhoto.setImageBitmap(resource)
          resource?.let { bitmap ->
            Palette.Builder(bitmap).generate {
              it?.let { palette ->
                context?.let { context ->
                  val dominantColor = palette.getDominantColor(
                    ContextCompat.getColor(context, R.color.black)
                  )
                  root.setBackgroundColor(dominantColor)
                }
              }
            }
          }
          return true
        }
      })
      .diskCacheStrategy(DiskCacheStrategy.ALL)
      .into(ivPhoto)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    binding = null
  }
}
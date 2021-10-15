package com.github.mhelmi.unsplashphotos.ui.photos.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.github.mhelmi.unsplashphotos.databinding.ItemAdBinding
import com.github.mhelmi.unsplashphotos.databinding.ItemPhotoBinding
import com.github.mhelmi.unsplashphotos.domain.photos.model.Ad
import com.github.mhelmi.unsplashphotos.domain.photos.model.Photo

class PhotosAdapter(
  val listener: PhotosClickListener
) : ListAdapter<Any, RecyclerView.ViewHolder>(PhotosDiffCallback) {

  override fun getItemViewType(position: Int): Int {
    return when (getItem(position)) {
      is Photo -> PHOTO_ITEM
      else -> AD_ITEM
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return when (viewType) {
      PHOTO_ITEM -> PhotoViewHolder(
        ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      )
      else -> AdViewHolder(
        ItemAdBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      )
    }
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val item = getItem(position)
    when (holder) {
      is PhotoViewHolder -> holder.bindItem(item as Photo)
      is AdViewHolder -> holder.bindItem(item as Ad)
    }
  }

  inner class PhotoViewHolder(
    private val binding: ItemPhotoBinding
  ) : RecyclerView.ViewHolder(binding.root) {

    fun bindItem(photo: Photo) = binding.apply {
      tvAuthorName.text = photo.author
      Glide.with(root)
        .load(photo.photoUrl)
        .into(ivPhoto)

      root.setOnClickListener {
        listener.onPhotoClick(bindingAdapterPosition, photo)
      }
    }
  }

  inner class AdViewHolder(
    private val binding: ItemAdBinding
  ) : RecyclerView.ViewHolder(binding.root) {

    fun bindItem(ad: Ad) = binding.apply {
      Glide.with(root)
        .load(ad.imageRes)
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        .into(ivAdPhoto)

      root.setOnClickListener {
        listener.onAdClick(bindingAdapterPosition, ad)
      }
    }
  }

  private object PhotosDiffCallback : DiffUtil.ItemCallback<Any>() {
    override fun areItemsTheSame(
      oldItem: Any,
      newItem: Any,
    ): Boolean {
      return (oldItem is Photo && newItem is Photo && oldItem.id == newItem.id) || (oldItem is Ad && newItem is Ad)
    }

    override fun areContentsTheSame(
      oldItem: Any,
      newItem: Any,
    ): Boolean {
      return (oldItem is Photo && newItem is Photo && oldItem == newItem) || (oldItem is Ad && newItem is Ad)
    }
  }

  interface PhotosClickListener {
    fun onPhotoClick(position: Int, photo: Photo)
    fun onAdClick(position: Int, ad: Ad)
  }

  companion object {
    const val PHOTO_ITEM = 1
    const val AD_ITEM = 2
  }
}
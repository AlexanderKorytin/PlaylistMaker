package com.example.playlistmaker.data.network

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.playlistmaker.data.ImageLoader

class ImageLoaderGlide: ImageLoader {

    override fun loadImage(url: String, placeholder: Int, imageView: ImageView) {
        Glide
            .with(imageView.context)
            .load(url)
            .placeholder(placeholder)
            .centerCrop()
            .into(imageView)
    }
}
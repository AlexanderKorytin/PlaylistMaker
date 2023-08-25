package com.example.playlistmaker.data.dto

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.domain.api.ImageLoader

class ImageLoaderGlide: ImageLoader {

    override fun loadImage(url: String, placeholder: Int, imageView: ImageView, round: Int) {
        Glide
            .with(imageView.context)
            .load(url)
            .placeholder(placeholder)
            .centerCrop()
            .transform(RoundedCorners(round))
            .into(imageView)
    }

}
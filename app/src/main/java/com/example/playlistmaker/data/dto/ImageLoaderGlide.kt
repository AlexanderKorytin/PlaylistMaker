package com.example.playlistmaker.data.dto

import android.widget.ImageView
import androidx.annotation.Px
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.domain.api.ImageLoader

class ImageLoaderGlide(private  val imageView: ImageView) : ImageLoader {
    override fun loadImage(url: String, placeholder: Int, @Px round: Int) {
        Glide
            .with(imageView.context)
            .load(url)
            .placeholder(placeholder)
            .centerCrop()
            .transform(RoundedCorners(round))
            .into(imageView)
    }

}
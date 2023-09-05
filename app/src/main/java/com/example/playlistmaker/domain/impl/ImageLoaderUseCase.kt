package com.example.playlistmaker.domain.impl

import android.widget.ImageView
import androidx.annotation.Px
import com.example.playlistmaker.domain.api.ImageLoader

class ImageLoaderUseCase(private val imageLoader: ImageLoader) {
    fun execute(url: String, placeholder: Int, @Px round: Int) {
        imageLoader.loadImage(url, placeholder, round)
    }
}
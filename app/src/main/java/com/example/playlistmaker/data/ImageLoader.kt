package com.example.playlistmaker.data

import android.widget.ImageView
import androidx.constraintlayout.widget.Placeholder

interface ImageLoader {
    fun loadImage(url: String, placeholder: Int,imageView: ImageView)
}
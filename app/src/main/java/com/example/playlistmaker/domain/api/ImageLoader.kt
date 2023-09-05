package com.example.playlistmaker.domain.api

import android.widget.ImageView

interface ImageLoader {
    fun loadImage(url: String, placeholder: Int, round: Int)
}
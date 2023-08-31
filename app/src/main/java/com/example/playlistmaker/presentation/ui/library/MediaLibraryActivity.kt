package com.example.playlistmaker.presentation.ui.library

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivityMediaLibraryBinding
import com.example.playlistmaker.presentation.setOnClickListenerWithViber

class MediaLibraryActivity : AppCompatActivity() {

    private lateinit var bindingMediaLibraryActivity: ActivityMediaLibraryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingMediaLibraryActivity = ActivityMediaLibraryBinding.inflate(layoutInflater)
        val view = bindingMediaLibraryActivity.root
        setContentView(view)

        bindingMediaLibraryActivity.backMediaLibray.setOnClickListenerWithViber {
            finish()
        }
    }
}
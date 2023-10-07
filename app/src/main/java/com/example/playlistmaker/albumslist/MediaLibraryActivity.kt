package com.example.playlistmaker.albumslist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.app.setOnClickListenerWithViber
import com.example.playlistmaker.databinding.ActivityMediaLibraryBinding
import com.google.android.material.tabs.TabLayoutMediator

class MediaLibraryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaLibraryBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaLibraryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.viewPagerMediaLibrary.adapter = MediaLibraryPagerAdapter(
            supportFragmentManager,
            lifecycle
        )

        tabMediator = TabLayoutMediator(
            binding.tabLayoutMediaLibrary,
            binding.viewPagerMediaLibrary
        ) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.favorite_tracks)
                1 -> tab.text = getString(R.string.Albums)
            }
        }
        tabMediator.attach()

        binding.backMediaLibray.setOnClickListenerWithViber {
            this.onBackPressedDispatcher.onBackPressed()
        }
    }
}
package com.example.playlistmaker.main.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.util.setOnClickListenerWithViber
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.library.ui.MediaLibraryActivity
import com.example.playlistmaker.search.ui.FindActivity
import com.example.playlistmaker.settings.ui.SettingsActivity

class MainActivity : AppCompatActivity() {
    private lateinit var bindingMain: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingMain = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingMain.root)
        bindingMain.find.setOnClickListenerWithViber {
            val findIntent = Intent(this, FindActivity::class.java)
            startActivity(findIntent)
        }

        bindingMain.media.setOnClickListenerWithViber {
            val mediaIntent = Intent(this, MediaLibraryActivity::class.java)
            startActivity(mediaIntent)
        }

        bindingMain.settings.setOnClickListenerWithViber {
            val settinsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settinsIntent)
        }

    }
}
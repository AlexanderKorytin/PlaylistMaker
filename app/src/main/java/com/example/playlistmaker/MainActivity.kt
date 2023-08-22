package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.playlistmaker.ui.Find.FindActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val buttonFind = findViewById<Button>(R.id.find)
        val buttonMedia = findViewById<Button>(R.id.media)
        val buttonSettings = findViewById<Button>(R.id.settings)

        buttonFind.setOnClickListenerWithViber {
            val findIntent = Intent(this, FindActivity::class.java)
            startActivity(findIntent)
        }

        buttonMedia.setOnClickListenerWithViber {
            val mediaIntent = Intent(this, MediaLibraryActivity::class.java)
            startActivity(mediaIntent)
        }

        buttonSettings.setOnClickListenerWithViber {
            val settinsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settinsIntent)
        }

    }
}
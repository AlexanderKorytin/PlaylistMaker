package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonFind = findViewById<Button>(R.id.find)
        val buttonMedia = findViewById<Button>(R.id.media)
        val buttonSettings = findViewById<Button>(R.id.settings)

        val findIntent = Intent(this, FindActivity::class.java)
        val buttonFindOnClickListener = object : View.OnClickListener {
            override fun onClick(p0: View?) {
                startActivity(findIntent)
            }
        }

        buttonFind.setOnClickListener(buttonFindOnClickListener)

        buttonMedia.setOnClickListener {
            val mediaIntent = Intent(this, MediaActivity::class.java)
            startActivity(mediaIntent)
        }

        buttonSettings.setOnClickListener {
            val settinsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settinsIntent)
        }

    }
}
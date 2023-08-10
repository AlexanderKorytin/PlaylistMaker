package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sharedPreferencesMainActivity =
            getSharedPreferences(
                TRACK_HISTORY_SHAREDPREFERENCES,
                MODE_PRIVATE
            )
        val buttonFind = findViewById<Button>(R.id.find)
        val buttonMedia = findViewById<Button>(R.id.media)
        val buttonSettings = findViewById<Button>(R.id.settings)

        buttonFind.setOnClickListenerWithViber {
            val findIntent = Intent(this, FindActivity::class.java)
            startActivity(findIntent)
        }

        buttonMedia.setOnClickListenerWithViber {
            val trackListJson = sharedPreferencesMainActivity.getString(SEARCH_HISTORY_TRACK_LIST, null)
            val mediaIntent = Intent(this, MediaActivity::class.java)
            if (trackListJson != null){
                val trackList = getTrackList(trackListJson)
                val track = Gson().toJson(trackList[0])
                mediaIntent.putExtra("clickedTrack", track)
            }
            startActivity(mediaIntent)
        }

        buttonSettings.setOnClickListenerWithViber {
            val settinsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settinsIntent)
        }

    }
}
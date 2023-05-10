package com.example.playlistmaker

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

        /*val buttonFindOnClickListener = object : View.OnClickListener {
            override fun onClick(p0: View?) {
                Toast.makeText(this@MainActivity, "Нажали кнопку Поиск!", Toast.LENGTH_SHORT).show()
            }
        }*/

        buttonFind.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                Toast.makeText(this@MainActivity, "Нажали кнопку Поиск!", Toast.LENGTH_SHORT).show()
            }
        })

        buttonMedia.setOnClickListener {
            Toast.makeText(
                this@MainActivity, "Нажали кнопку Медиатека!", Toast.LENGTH_SHORT
            ).show()
        }

        buttonSettings.setOnClickListener {
            Toast.makeText(
                this@MainActivity, "Нажали кнопку Настройки!", Toast.LENGTH_SHORT
            ).show()
        }

    }
}
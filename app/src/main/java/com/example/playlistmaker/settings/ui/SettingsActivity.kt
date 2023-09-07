package com.example.playlistmaker.settings.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.Util.APP_SETTINGS_PREF_KEY
import com.example.playlistmaker.Util.App
import com.example.playlistmaker.Util.DARK_THEME
import com.example.playlistmaker.Util.setOnClickListenerWithViber
import com.example.playlistmaker.Util.setVibe
import com.example.playlistmaker.settings.ui.viewmodel.SettingsViewModelFactory
import com.example.playlistmaker.settings.ui.viewmodel.SettingsViewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var SettinsVM: SettingsViewModel

    @SuppressLint("MissingInflatedId", "UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val back = findViewById<ImageView>(R.id.backSetting)
        val nightTheme = findViewById<SwitchCompat>(R.id.themeSwitch)
        val writeToSupport = findViewById<TextView>(R.id.writeToSupport)
        val shareApp = findViewById<TextView>(R.id.shareApp)
        val userAgreement = findViewById<TextView>(R.id.userAgreement)
        val switchPreference = getSharedPreferences(APP_SETTINGS_PREF_KEY, MODE_PRIVATE)
        nightTheme.isChecked = (applicationContext as App).darkTheme

        SettinsVM =
            ViewModelProvider(this, SettingsViewModelFactory(this))[SettingsViewModel::class.java]

        back.setOnClickListenerWithViber {
            finish()
        }

        nightTheme.setOnCheckedChangeListener { switcher, checked ->
            applicationContext.setVibe()
            (applicationContext as App).switchTheme(checked)
            switchPreference.edit()
                .putBoolean(DARK_THEME, checked)
                .apply()
        }

        shareApp.setOnClickListenerWithViber {
            SettinsVM.shareApp()
        }

        writeToSupport.setOnClickListenerWithViber {
            SettinsVM.writeToSupport()
        }

        userAgreement.setOnClickListenerWithViber {
            SettinsVM.seeUserAgreement()
        }

    }
}
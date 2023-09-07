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
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.settings.ui.viewmodel.SettingsViewModelFactory
import com.example.playlistmaker.settings.ui.viewmodel.SettingsViewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var SettinsVM: SettingsViewModel
    private lateinit var bindingSettings: ActivitySettingsBinding

    @SuppressLint("MissingInflatedId", "UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingSettings = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(bindingSettings.root)

        val switchPreference = getSharedPreferences(APP_SETTINGS_PREF_KEY, MODE_PRIVATE)
        bindingSettings.themeSwitch.isChecked = (applicationContext as App).darkTheme

        SettinsVM =
            ViewModelProvider(this, SettingsViewModelFactory(this))[SettingsViewModel::class.java]

        bindingSettings.backSetting.setOnClickListenerWithViber {
            finish()
        }

        bindingSettings.themeSwitch.setOnCheckedChangeListener { switcher, checked ->
            applicationContext.setVibe()
            (applicationContext as App).switchTheme(checked)
            switchPreference.edit()
                .putBoolean(DARK_THEME, checked)
                .apply()
        }

        bindingSettings.shareApp.setOnClickListenerWithViber {
            SettinsVM.shareApp()
        }

        bindingSettings.writeToSupport.setOnClickListenerWithViber {
            SettinsVM.writeToSupport()
        }

        bindingSettings.userAgreement.setOnClickListenerWithViber {
            SettinsVM.seeUserAgreement()
        }

    }
}
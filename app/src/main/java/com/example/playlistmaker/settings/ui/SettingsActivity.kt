package com.example.playlistmaker.settings.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.Util.setOnClickListenerWithViber
import com.example.playlistmaker.Util.setVibe
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.settings.ui.viewmodel.SettingsViewModel
import com.example.playlistmaker.settings.ui.viewmodel.SettingsViewModelFactory

class SettingsActivity : AppCompatActivity() {
    private lateinit var SettinsVM: SettingsViewModel
    private lateinit var bindingSettings: ActivitySettingsBinding

    @SuppressLint("MissingInflatedId", "UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingSettings = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(bindingSettings.root)
        SettinsVM =
            ViewModelProvider(this, SettingsViewModelFactory(this))[SettingsViewModel::class.java]

        SettinsVM.getCurrentTheme().observe(this) {
            bindingSettings.themeSwitch.isChecked = it.isNight
        }

        bindingSettings.backSetting.setOnClickListenerWithViber {
            finish()
        }

        bindingSettings.themeSwitch.setOnCheckedChangeListener { switcher, checked ->
            applicationContext.setVibe()
            SettinsVM.updateNightTheme(checked)
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
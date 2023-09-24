package com.example.playlistmaker.settings.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.app.setOnClickListenerWithViber
import com.example.playlistmaker.app.setVibe
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.settings.ui.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {
    private val SettinsVM: SettingsViewModel by viewModel<SettingsViewModel>()
    private lateinit var bindingSettings: ActivitySettingsBinding

    @SuppressLint("MissingInflatedId", "UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingSettings = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(bindingSettings.root)

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
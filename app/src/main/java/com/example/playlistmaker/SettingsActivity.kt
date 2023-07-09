package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat

class SettingsActivity : AppCompatActivity() {
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
            val shareAppIntent = Intent(Intent.ACTION_SEND)
            shareAppIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app_text))
            shareAppIntent.type = "text/plain"
            startActivity(shareAppIntent)
        }

        writeToSupport.setOnClickListenerWithViber {
            val message = getString(R.string.mail_support_massege)
            val writeSupportIntent = Intent(Intent.ACTION_SENDTO)
            writeSupportIntent.data = Uri.parse("mailto:")
            writeSupportIntent.putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf(getString(R.string.my_email_adress))
            )
            writeSupportIntent.putExtra(
                Intent.EXTRA_SUBJECT,
                getString(R.string.mail_support_subject)
            )
            writeSupportIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(writeSupportIntent)
        }

        userAgreement.setOnClickListenerWithViber {
            val userAgreementIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.user_agreement_uri)))
            startActivity(userAgreementIntent)
        }

    }
}
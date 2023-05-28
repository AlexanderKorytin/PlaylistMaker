package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
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

        back.setOnClickListener {
            finish()
        }

        nightTheme.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

                false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        shareApp.setOnClickListener {
            val shareAppIntent = Intent(Intent.ACTION_SEND)
            shareAppIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app_text))
            shareAppIntent.setType("text/plain")
            startActivity(shareAppIntent)
        }

        writeToSupport.setOnClickListener {
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

        userAgreement.setOnClickListener {
            val userAgreementIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.user_agreement_uri)))
            startActivity(userAgreementIntent)
        }

    }
}
package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val back = findViewById<ImageView>(R.id.backSetting)
        val writeToSupport = findViewById<TextView>(R.id.writeToSupport)
        val shareApp = findViewById<TextView>(R.id.shareApp)
        val userAgreement = findViewById<TextView>(R.id.userAgreement)

        back.setOnClickListener {
            finish()
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
            writeSupportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("koritin@list.ru", "koritin84@gmail.com"))
            writeSupportIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_support_subject))
            writeSupportIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(writeSupportIntent)
        }

        userAgreement.setOnClickListener{
            val userAgreementIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.user_agreement_uri)))
            startActivity(userAgreementIntent)
        }

    }
}
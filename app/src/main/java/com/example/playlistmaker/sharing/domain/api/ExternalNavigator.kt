package com.example.playlistmaker.sharing.domain.api

import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.domain.models.EmailData

interface ExternalNavigator {

    fun shareLink(appLink: String, errorMessage: String)
    fun openLink(link: String)
    fun openEmail(emailData: EmailData)
}
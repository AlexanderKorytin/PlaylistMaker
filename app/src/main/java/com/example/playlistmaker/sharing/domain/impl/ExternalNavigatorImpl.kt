package com.example.playlistmaker.sharing.domain.impl

import android.content.ActivityNotFoundException
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.playlistmaker.sharing.domain.api.ExternalNavigator
import com.example.playlistmaker.sharing.domain.models.EmailData

class ExternalNavigatorImpl: ExternalNavigator {
    override fun shareLink(appLink: String, errorMessage: String){

    }
    override fun openLink(link: String){

    }
    override fun openEmail(emailData: EmailData){

    }

}
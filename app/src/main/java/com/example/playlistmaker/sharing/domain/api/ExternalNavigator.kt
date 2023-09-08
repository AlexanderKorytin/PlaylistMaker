package com.example.playlistmaker.sharing.domain.api

import com.example.playlistmaker.sharing.domain.models.EmailData

interface ExternalNavigator {
    val emailData: EmailData

    val Applink: String

    val userLink: String

    fun shareLink(appLink: String)
    fun openLink(link: String)
    fun openEmail(emailData: EmailData)
}
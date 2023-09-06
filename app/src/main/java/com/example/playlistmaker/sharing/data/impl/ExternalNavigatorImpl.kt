package com.example.playlistmaker.sharing.data.impl

import com.example.playlistmaker.sharing.domain.api.ExternalNavigator
import com.example.playlistmaker.sharing.domain.models.EmailData

class ExternalNavigatorImpl: ExternalNavigator {
    override fun shareLink(appLink: String, errorMessage: String) {
        TODO("Not yet implemented")
    }

    override fun openLink(link: String) {
        TODO("Not yet implemented")
    }

    override fun openEmail(emailData: EmailData) {
        TODO("Not yet implemented")
    }
}
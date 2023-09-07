package com.example.playlistmaker.sharing.domain.impl

import com.example.playlistmaker.sharing.domain.api.ExternalNavigator
import com.example.playlistmaker.sharing.domain.api.SharingInteractor
import com.example.playlistmaker.sharing.domain.models.EmailData

class SharingInteractorImpl(private val externalNavigator: ExternalNavigator): SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return externalNavigator.Applink
    }
    private fun getSupportEmailData(): EmailData {
        return externalNavigator.emailData
    }

    private fun getTermsLink(): String {
        return externalNavigator.userLink
    }
}
package com.example.playlistmaker.sharing.data.impl

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.domain.api.ExternalNavigator
import com.example.playlistmaker.sharing.domain.models.EmailData

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {

    override val emailData = EmailData(
        adressEmail = context.getString(R.string.my_email_adress),
        themeEmail = context.getString(R.string.mail_support_subject),
        messageEmail = context.getString(R.string.mail_support_massege)
    )
    override val Applink = context.getString(R.string.share_app_text)

    override val userLink = context.getString(R.string.user_agreement_uri)

    override fun shareLink(appLink: String) {
        val shareAppIntent = Intent(Intent.ACTION_SEND)
        shareAppIntent.putExtra(Intent.EXTRA_TEXT, appLink)
        shareAppIntent.type = "text/plain"
        shareAppIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(shareAppIntent)
        } catch (activityNotFound: ActivityNotFoundException) {
            Toast.makeText(context, context.getString(R.string.app_not_found), Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun openLink(link: String) {
        val userAgreementIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse(link))
        userAgreementIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(userAgreementIntent)
        } catch (activityNotFound: ActivityNotFoundException) {
            Toast.makeText(context, context.getString(R.string.app_not_found), Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun openEmail(emailData: EmailData) {
        val message = emailData.messageEmail
        val writeSupportIntent = Intent(Intent.ACTION_SENDTO)
        writeSupportIntent.data = Uri.parse("mailto:")
        writeSupportIntent.putExtra(
            Intent.EXTRA_EMAIL,
            arrayOf(emailData.adressEmail)
        )
        writeSupportIntent.putExtra(
            Intent.EXTRA_SUBJECT,
            emailData.themeEmail
        )
        writeSupportIntent.putExtra(Intent.EXTRA_TEXT, message)
        writeSupportIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(writeSupportIntent)
        } catch (activityNotFound: ActivityNotFoundException) {
            Toast.makeText(context, context.getString(R.string.app_not_found), Toast.LENGTH_SHORT)
                .show()
        }
    }
}
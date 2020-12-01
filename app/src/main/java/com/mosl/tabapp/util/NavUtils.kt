package com.mosl.tabapp.util

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import timber.log.Timber


private const val SMS_URI = "smsto:"
private const val SMS_EXTRA = "sms_body"
private const val CALL_DATA = "tel:"
private const val EMAIL_TYPE = "message/rfc822"

fun Context.openPDF(url: String) {
    val path = Uri.parse(url)
    val pdfIntent = Intent(Intent.ACTION_VIEW)
    pdfIntent.setDataAndType(path, "application/pdf")
    pdfIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(pdfIntent)
}

fun Context.downloadDocument(url: String) {
    val request = DownloadManager.Request(Uri.parse(url))
    request.setDescription("Downloading...")
    request.allowScanningByMediaScanner()
    request.setNotificationVisibility(
        DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
    )

    val dm = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    dm.enqueue(request)
}

fun Context.downloadImage(url: String): Long {
    val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    val request = DownloadManager.Request(Uri.parse(url))
        .setNotificationVisibility(
            DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
        )
    val downloadReference = downloadManager.enqueue(request)
    Timber.d("Download Requested for Reference Id %d", downloadReference)
    return downloadReference
}

fun Context.initiateMessage(
    number: String,
    eventMessage: String? = null
) {
    val smsIntent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse("$SMS_URI${Uri.encode(number)}")
        eventMessage?.let { putExtra(SMS_EXTRA, it) }
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    startActivity(smsIntent)
}

fun Context.initiateCall(
    number: String
) {
    val callIntent = Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("$CALL_DATA$number")
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    startActivity(callIntent)
}

fun Context.initialEmail(
    emailId: String
) {
    var emailIntent = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_EMAIL, emailId)
        type = EMAIL_TYPE
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    emailIntent = Intent.createChooser(emailIntent, "Choose an Email client :")
    startActivity(emailIntent)
}

fun Context.openWebUrl(
    url: Uri
) {
    Timber.d("Open URL : %s", url.toString())
    openUri(this, url)
}

fun openUri(
    context: Context,
    uri: Uri
) {
    Timber.d("Open Uri : %s", uri.toString())
    val intent = Intent(Intent.ACTION_VIEW, uri)
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    }
}
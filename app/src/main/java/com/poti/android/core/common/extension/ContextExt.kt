package com.poti.android.core.common.extension

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.core.content.getSystemService
import androidx.core.net.toUri
import com.poti.android.R
import timber.log.Timber

private const val X_PACKAGE_NAME = "com.twitter.android"
private const val X_WEB_INTENT_URL = "https://x.com/intent/post?text="

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.copyToClipboard(
    text: String,
    label: String = getString(R.string.clipboard_default_label),
) {
    val clipboardManager = getSystemService<ClipboardManager>() ?: return
    clipboardManager.setPrimaryClip(ClipData.newPlainText(label, text))
}

fun Context.shareText(
    title: String,
    text: String,
) {
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    startActivity(Intent.createChooser(sendIntent, title))
}

fun Context.shareTextToX(text: String) {
    val appIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
        setPackage(X_PACKAGE_NAME)
        addFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK or
                Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS,
        )
    }
    val webIntent = Intent(Intent.ACTION_VIEW, "$X_WEB_INTENT_URL${Uri.encode(text)}".toUri())

    runCatching { startActivity(appIntent) }
        .recoverCatching { startActivity(webIntent) }
        .onFailure { Timber.e(it, "X 공유 실패") }
}

fun Context.openSystemNotificationSetting() {
    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
        .putExtra(Settings.EXTRA_APP_PACKAGE, packageName)

    runCatching { startActivity(intent) }
        .onFailure { Timber.w(it, "Unable to open system notification setting") }
}

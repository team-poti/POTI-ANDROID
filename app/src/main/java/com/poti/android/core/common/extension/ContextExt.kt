package com.poti.android.core.common.extension

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.net.toUri

private const val X_PACKAGE_NAME = "com.twitter.android"
private const val PLAY_STORE_MARKET_URL = "market://details?id="
private const val PLAY_STORE_WEB_URL = "https://play.google.com/store/apps/details?id="

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.shareText(title: String, text: String) {
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    startActivity(Intent.createChooser(sendIntent, title))
}

fun Context.shareTextToX(text: String) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
        setPackage(X_PACKAGE_NAME)
        addFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK or
                Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS,
        )
    }

    runCatching { startActivity(shareIntent) }
        .recoverCatching { openPlayStore(X_PACKAGE_NAME) }
}

fun Context.openPlayStore(packageName: String) {
    val marketIntent = Intent(Intent.ACTION_VIEW, "$PLAY_STORE_MARKET_URL$packageName".toUri())
    val webIntent = Intent(Intent.ACTION_VIEW, "$PLAY_STORE_WEB_URL$packageName".toUri())

    runCatching { startActivity(marketIntent) }
        .recoverCatching { startActivity(webIntent) }
}

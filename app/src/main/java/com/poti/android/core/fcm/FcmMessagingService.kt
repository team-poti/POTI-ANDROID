package com.poti.android.core.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.poti.android.R
import com.poti.android.core.fcm.repository.FcmRepository
import com.poti.android.data.local.datasource.PreferenceDataSource
import com.poti.android.di.ApplicationScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

private val Context.notificationManager: NotificationManager
    get() = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

@AndroidEntryPoint
class FcmMessagingService : FirebaseMessagingService() {
    @ApplicationScope
    @Inject
    lateinit var scope: CoroutineScope

    @Inject
    lateinit var fcmRepository: FcmRepository

    @Inject
    lateinit var preferenceDataSource: PreferenceDataSource

    @Suppress("OVERRIDE_DEPRECATION")
    override fun onNewToken(token: String) {
        if (preferenceDataSource.cachedAccessToken != null) {
            scope.launch {
                fcmRepository.saveFcmToken(token)
                    .onFailure { Timber.e(it, "Failed to save FCM token") }
            }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Timber.d("FCM data = ${message.data}")

        val title = message.data["title"] ?: return Timber.e("Fcm title is null")
        val body = message.data["body"] ?: return Timber.e("Fcm body is null")
        val deeplink = message.data["deeplink"]

        showNotification(
            title = title,
            body = body,
            deepLink = deeplink,
        )
    }

    private fun showNotification(
        title: String,
        body: String,
        deepLink: String?,
    ) {
        val id = System.currentTimeMillis().toInt()
        val pendingIntent = buildClickIntent(deepLink)?.let {
            PendingIntent.getActivity(
                this,
                id,
                it,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
        }
        val notification = NotificationCompat.Builder(this, DEFAULT_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle(title)
            .setContentText(body)
            .apply { pendingIntent?.let { setContentIntent(it) } }
            .setAutoCancel(true)
            .build()

        notificationManager.notify(id, notification)
    }

    private fun buildClickIntent(deepLink: String?): Intent? {
        val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
        if (launchIntent == null) {
            Timber.e("Launch intent not found for package: $packageName")
            return null
        }

        val intent = if (deepLink.isNullOrBlank()) {
            launchIntent
        } else {
            Intent(Intent.ACTION_VIEW, deepLink.toUri()).apply {
                component = launchIntent.component
            }
        }

        return intent.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
    }

    companion object {
        private const val DEFAULT_CHANNEL_ID = "default_channel_id"
        private const val DEFAULT_CHANNEL_NAME = "기본 알림"

        fun createChannels(context: Context) {
            context.notificationManager.createNotificationChannel(
                NotificationChannel(
                    DEFAULT_CHANNEL_ID,
                    DEFAULT_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT,
                ),
            )
        }
    }
}

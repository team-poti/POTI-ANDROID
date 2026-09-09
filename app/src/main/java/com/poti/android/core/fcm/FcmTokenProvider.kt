package com.poti.android.core.fcm

import com.google.firebase.messaging.FirebaseMessaging
import com.poti.android.BuildConfig
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class FcmTokenProvider @Inject constructor() {
    @Suppress("DEPRECATION")
    suspend fun getToken(): String? {
        if (!BuildConfig.FIREBASE_ENABLED) return null

        return suspendCancellableCoroutine { continuation ->
            FirebaseMessaging.getInstance().token
                .addOnSuccessListener { continuation.resume(it) }
                .addOnFailureListener {
                    Timber.e(it, "Failed to get FCM token")
                    continuation.resume(null)
                }
        }
    }
}

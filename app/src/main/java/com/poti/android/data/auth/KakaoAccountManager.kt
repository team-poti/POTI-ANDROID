package com.poti.android.data.auth

import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class KakaoAccountManager @Inject constructor() {
    suspend fun unlink() {
        suspendCancellableCoroutine { continuation ->
            UserApiClient.instance.unlink { error ->
                if (!continuation.isActive) return@unlink

                if (error == null) {
                    continuation.resume(Unit)
                } else {
                    continuation.resumeWithException(error)
                }
            }
        }
    }
}

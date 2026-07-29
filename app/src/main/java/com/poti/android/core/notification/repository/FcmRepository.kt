package com.poti.android.core.notification.repository

interface FcmRepository {
    suspend fun deleteFcmToken(token: String): Result<Unit>

    suspend fun saveFcmToken(token: String): Result<Unit>
}

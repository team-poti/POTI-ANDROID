package com.poti.android.domain.repository

import com.poti.android.domain.model.auth.UserAuth

interface AuthRepository {
    suspend fun login(
        socialType: String,
        token: String,
    ): Result<UserAuth>

    suspend fun saveOnboardingState(isCompleted: Boolean)
}

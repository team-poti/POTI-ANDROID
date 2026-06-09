package com.poti.android.domain.repository

import com.poti.android.domain.model.auth.AuthState
import com.poti.android.domain.model.auth.UserAuth
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun observeAuthState(): Flow<AuthState>

    suspend fun login(
        socialType: String,
        token: String,
    ): Result<UserAuth>

    suspend fun saveOnboardingState(isCompleted: Boolean): Result<Unit>

    suspend fun withdrawal(): Result<Unit>
}

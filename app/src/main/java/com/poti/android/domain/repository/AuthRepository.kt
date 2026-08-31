package com.poti.android.domain.repository

import com.poti.android.domain.model.auth.AuthState
import com.poti.android.domain.model.auth.SocialType
import com.poti.android.domain.model.auth.UserAuth
import com.poti.android.domain.model.auth.WithdrawalReason
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun observeAuthState(): Flow<AuthState>

    suspend fun login(
        socialType: SocialType,
        token: String,
    ): Result<UserAuth>

    suspend fun saveOnboardingState(isCompleted: Boolean): Result<Unit>

    suspend fun logout(): Result<Unit>

    suspend fun getWithdrawalReasons(): Result<List<WithdrawalReason>>

    suspend fun withdrawal(reason: String): Result<Unit>
}

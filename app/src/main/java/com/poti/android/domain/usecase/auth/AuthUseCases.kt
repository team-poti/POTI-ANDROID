package com.poti.android.domain.usecase.auth

import com.poti.android.domain.model.auth.AuthState
import com.poti.android.domain.model.auth.UserAuth
import com.poti.android.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveAuthStateUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    operator fun invoke(): Flow<AuthState> = authRepository.observeAuthState()
}

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(
        socialType: String,
        token: String,
    ): Result<UserAuth> = authRepository.login(socialType, token)
}

class WithdrawalUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(): Result<Unit> = authRepository.withdrawal()
}

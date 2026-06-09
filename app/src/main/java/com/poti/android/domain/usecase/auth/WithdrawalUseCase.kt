package com.poti.android.domain.usecase.auth

import com.poti.android.domain.repository.AuthRepository
import javax.inject.Inject

class WithdrawalUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(): Result<Unit> = authRepository.withdrawal()
}

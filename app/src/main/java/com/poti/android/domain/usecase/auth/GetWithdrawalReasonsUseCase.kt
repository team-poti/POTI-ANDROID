package com.poti.android.domain.usecase.auth

import com.poti.android.domain.model.auth.WithdrawalReason
import com.poti.android.domain.repository.AuthRepository
import javax.inject.Inject

class GetWithdrawalReasonsUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(): Result<List<WithdrawalReason>> =
        authRepository.getWithdrawalReasons()
}

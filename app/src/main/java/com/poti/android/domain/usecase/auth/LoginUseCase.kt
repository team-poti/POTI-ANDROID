package com.poti.android.domain.usecase.auth

import com.poti.android.domain.model.auth.SocialType
import com.poti.android.domain.model.auth.UserAuth
import com.poti.android.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(
        socialType: SocialType,
        token: String,
    ): Result<UserAuth> = authRepository.login(socialType, token)
}

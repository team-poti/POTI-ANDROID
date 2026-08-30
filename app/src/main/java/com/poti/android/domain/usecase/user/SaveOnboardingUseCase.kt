package com.poti.android.domain.usecase.user

import com.poti.android.domain.repository.AuthRepository
import com.poti.android.domain.repository.UserRepository
import javax.inject.Inject

class SaveOnboardingUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(
        nickname: String,
        favoriteArtistId: Long?,
    ): Result<Unit> =
        userRepository.patchOnboarding(nickname, favoriteArtistId)
            .mapCatching { authRepository.saveOnboardingState(isCompleted = true).getOrThrow() }
}

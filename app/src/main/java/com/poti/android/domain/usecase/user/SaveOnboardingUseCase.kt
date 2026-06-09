package com.poti.android.domain.usecase.user

import com.poti.android.domain.repository.UserRepository
import javax.inject.Inject

class SaveOnboardingUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(
        nickname: String,
        favoriteArtistId: Long?,
    ): Result<Unit> = userRepository.patchOnboarding(nickname, favoriteArtistId)
}

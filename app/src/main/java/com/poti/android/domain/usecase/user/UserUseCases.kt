package com.poti.android.domain.usecase.user

import com.poti.android.domain.model.user.UserMyPage
import com.poti.android.domain.model.user.UserProfile
import com.poti.android.domain.repository.UserRepository
import javax.inject.Inject

class CheckNicknameDuplicationUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(nickname: String): Result<Boolean> =
        userRepository.postNicknameDuplicate(nickname)
}

class SaveOnboardingUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(
        nickname: String,
        favoriteArtistId: Long?,
    ): Result<Unit> = userRepository.patchOnboarding(nickname, favoriteArtistId)
}

class GetUserMyPageUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(): Result<UserMyPage> = userRepository.getUserMyPage()
}

class GetUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(userId: Long): Result<UserProfile> =
        userRepository.getUserProfile(userId)
}

package com.poti.android.domain.usecase.user

import com.poti.android.domain.model.user.UserProfile
import com.poti.android.domain.repository.UserRepository
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(userId: Long): Result<UserProfile> =
        userRepository.getUserProfile(userId)
}

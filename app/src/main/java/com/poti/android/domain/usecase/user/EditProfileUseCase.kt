package com.poti.android.domain.usecase.user

import com.poti.android.domain.repository.UserRepository
import javax.inject.Inject

class EditProfileUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(
        nickname: String,
        profileImageUrl: String,
    ): Result<Unit> = userRepository.patchProfile(
        nickname = nickname,
        profileImageUrl = profileImageUrl,
    )
}

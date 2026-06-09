package com.poti.android.domain.usecase.user

import com.poti.android.domain.repository.UserRepository
import javax.inject.Inject

class CheckNicknameDuplicationUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(nickname: String): Result<Boolean> =
        userRepository.postNicknameDuplicate(nickname)
}

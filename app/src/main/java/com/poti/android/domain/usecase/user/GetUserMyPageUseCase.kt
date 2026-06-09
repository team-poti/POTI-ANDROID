package com.poti.android.domain.usecase.user

import com.poti.android.domain.model.user.UserMyPage
import com.poti.android.domain.repository.UserRepository
import javax.inject.Inject

class GetUserMyPageUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(): Result<UserMyPage> = userRepository.getUserMyPage()
}

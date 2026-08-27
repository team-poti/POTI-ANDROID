package com.poti.android.domain.usecase.user

import com.poti.android.domain.model.user.UserAccount
import com.poti.android.domain.repository.UserRepository
import javax.inject.Inject

class GetUserAccountUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(): Result<UserAccount> = userRepository.getUserAccount()
}

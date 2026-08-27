package com.poti.android.core.common.util

import com.poti.android.R
import com.poti.android.core.network.model.NetworkError
import com.poti.android.presentation.onboarding.model.ErrorText

object NicknameValidator {
    const val MIN_LENGTH = 2

    private val NICKNAME_REGEX = "^[가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z0-9]*$".toRegex()

    fun validateFormat(nickname: String): ErrorText? = when {
        nickname.isEmpty() -> null
        nickname.length < MIN_LENGTH -> ErrorText.StringResource(R.string.onboarding_nickname_error_min_length)
        !nickname.matches(NICKNAME_REGEX) -> ErrorText.StringResource(R.string.onboarding_nickname_error_special_characters)
        else -> null
    }

    fun duplicateNicknameError(): ErrorText = ErrorText.StringResource(R.string.onboarding_nickname_error_duplicate)

    fun toDuplicationCheckError(error: Throwable): ErrorText = when {
        error !is NetworkError.BadRequest -> ErrorText.StringResource(R.string.onboarding_nickname_error_server)
        error.code == DUPLICATE_NICKNAME_ERROR_CODE -> ErrorText.StringResource(R.string.onboarding_nickname_error_duplicate)
        else -> ErrorText.StringResource(R.string.onboarding_nickname_error_special_characters)
    }

    private const val DUPLICATE_NICKNAME_ERROR_CODE = 40003
}

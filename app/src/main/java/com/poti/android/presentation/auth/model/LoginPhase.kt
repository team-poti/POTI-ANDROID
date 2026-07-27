package com.poti.android.presentation.auth.model

enum class LoginPhase {
    IDLE,
    SOCIAL_LOGIN,
    SERVER_LOGIN,
    SUCCESS,
    FAILURE,
    ;

    val isInProgress: Boolean
        get() = this == SOCIAL_LOGIN || this == SERVER_LOGIN

    val canStartLogin: Boolean
        get() = this == IDLE || this == FAILURE
}

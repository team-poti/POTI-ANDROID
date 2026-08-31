package com.poti.android.presentation.auth.model

enum class LoginPhase {
    IDLE,
    SOCIAL_LOGIN,
    SERVER_LOGIN,
    ;

    val isInProgress: Boolean
        get() = this != IDLE

    val canStartLogin: Boolean
        get() = this == IDLE
}

package com.poti.android.core.network.model

object AuthType {
    const val NO_AUTH = "No-Auth" // 인증 불필요
    const val BEARER = "Bearer" // Authorization: Bearer {token}
    const val RAW = "Raw" // Authorization: {token}
    const val ACCESS_TOKEN = "AccessToken" // Access-Token: {token}
}

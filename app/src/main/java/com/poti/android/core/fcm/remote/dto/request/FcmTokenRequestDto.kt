package com.poti.android.core.fcm.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FcmTokenRequestDto(
    @SerialName("token")
    val token: String,
    @SerialName("deviceType")
    val deviceType: String = DEVICE_TYPE,
) {
    private companion object {
        const val DEVICE_TYPE = "ANDROID"
    }
}

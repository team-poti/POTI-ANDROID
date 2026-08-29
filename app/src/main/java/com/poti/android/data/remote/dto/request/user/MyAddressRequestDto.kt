package com.poti.android.data.remote.dto.request.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MyAddressRequestDto(
    @SerialName("receiverName")
    val receiverName: String,
    @SerialName("zipcode")
    val zipcode: String,
    @SerialName("address")
    val address: String,
    @SerialName("addressDetail")
    val addressDetail: String,
    @SerialName("phone")
    val phone: String,
)

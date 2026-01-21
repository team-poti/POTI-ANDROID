package com.poti.android.data.remote.dto.request.party

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreatePartyRequestDto(
    @SerialName("artistId")
    val artistId: Long,
    @SerialName("title")
    val title: String,
    @SerialName("content")
    val content: String,
    @SerialName("deadline")
    val deadline: String,
    @SerialName("bankName")
    val bankName: String,
    @SerialName("accountNumber")
    val accountNumber: String,
    @SerialName("imageUrls")
    val imageUrls: List<String>,
    @SerialName("options")
    val options: List<OptionRequestDto>,
    @SerialName("shippings")
    val shippings: List<ShippingRequestDto>,
)

@Serializable
data class OptionRequestDto(
    @SerialName("memberId")
    val memberId: Long,
    @SerialName("price")
    val price: Int,
)

@Serializable
data class ShippingRequestDto(
    @SerialName("deliveryMethodId")
    val deliveryMethodId: Long,
    @SerialName("price")
    val price: Int,
)

package com.poti.android.data.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreatePostRequestDto(
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
    val options: List<OptionDto>,
    @SerialName("shippings")
    val shippings: List<ShippingDto>,
)

@Serializable
data class OptionDto(
    @SerialName("memberId")
    val memberId: Long,
    @SerialName("price")
    val price: Int,
)

@Serializable
data class ShippingDto(
    @SerialName("deliveryMethodId")
    val deliveryMethodId: Long,
    @SerialName("price")
    val price: Int,
)

package com.poti.android.domain.model.delivery

data class DeliveryInfo(
    val receiverName: String,
    val zipcode: String,
    val address: String,
    val addressDetail: String,
    val phoneNumber: String,
)

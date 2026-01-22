package com.poti.android.domain.model.history

import com.poti.android.domain.type.PartyStatusType

data class PartySummary(
    val imageUrl: String,
    val artist: String,
    val title: String,
    val partyStatus: PartyStatusType,
    val statusMessage: String,
)

data class ShippingInfo(
    val receiverName: String?,
    val address: String?,
    val phone: String?,
    val trackingNumber: String?,
)

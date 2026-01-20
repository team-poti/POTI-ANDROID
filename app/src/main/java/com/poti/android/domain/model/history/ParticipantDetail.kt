package com.poti.android.domain.model.history

import com.poti.android.domain.type.ParticipantStatusType

data class ParticipantDetail(
    val partyId: Long,
    val artistInfo: ArtistInfo,
    val progressInfo: ProgressInfo,
    val depositInfo: ParticipantDepositInfo,
    val shippingInfo: ParticipantShippingInfo,
    val userState: ParticipantStatusType,
)

data class ParticipantDepositInfo(
    val items: List<DepositItem>,
    val totalAmount: Int,
)

sealed interface DepositItem {
    val name: String
    val price: Int

    data class DeliveryItem(
        override val name: String,
        override val price: Int,
    ) : DepositItem

    data class MemberItem(
        override val name: String,
        override val price: Int,
    ) : DepositItem
}

data class ParticipantShippingInfo(
    val recipient: String,
    val zipcode: String,
    val address: String,
    val phone: String,
    val deliveryMethod: String,
    val trackingNumber: String?,
)

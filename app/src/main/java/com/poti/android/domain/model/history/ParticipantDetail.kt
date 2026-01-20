package com.poti.android.domain.model.history

import com.poti.android.domain.type.ParticipantStatusType

data class ParticipantDetail(
    val recruitId: Long,
    val userState: ParticipantStatusType,
    val partySummary: PartySummary,
    val progressInfo: ProgressInfo,
    val depositInfo: ParticipantDepositInfo,
    val shippingInfo: ParticipantShippingInfo,
    val recruiterName: String,
    val recruiterProfileUrl: String,
    val recruiterRating: String,
)

data class ParticipantDepositInfo(
    val items: List<DepositItem>,
    val totalAmount: Int,
    val depositStatus: DepositStatus,
)

sealed interface DepositStatus {
    val accountNumber: String
    val dueDate: String

    data class DepositWait(
        override val accountNumber: String,
        override val dueDate: String,
    ) : DepositStatus

    data class DepositCheck(
        override val accountNumber: String,
        override val dueDate: String,
    ) : DepositStatus

    object DepositDone : DepositStatus {
        override val accountNumber: String = ""
        override val dueDate: String = ""
    }
}

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

package com.poti.android.domain.model.history

import com.poti.android.domain.type.ParticipantStatusType

data class ParticipantDetail(
    val participationId: Long,
    val partySummary: PartySummary,
    val memberPayments: List<MemberPayment>,
    val paymentInfo: PaymentInfo,
    val shippingInfo: ShippingInfo,
)

data class MemberPayment(
    val memberName: String,
    val price: Int,
)

data class PaymentInfo(
    val shippingFee: Int,
    val totalAmount: Int,
    val depositStatus: ParticipantStatusType,
    val bank: String,
    val accountNumber: String,
    val depositDeadline: String,
)

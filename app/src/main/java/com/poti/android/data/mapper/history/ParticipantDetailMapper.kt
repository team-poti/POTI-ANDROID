package com.poti.android.data.mapper.history

import com.poti.android.data.remote.dto.response.history.ParticipantDetailResponseDto
import com.poti.android.domain.model.history.MemberPayment
import com.poti.android.domain.model.history.ParticipantDetail
import com.poti.android.domain.model.history.ParticipantShippingInfo
import com.poti.android.domain.model.history.PartySummary
import com.poti.android.domain.model.history.PaymentInfo
import com.poti.android.domain.type.ParticipantStatusType
import com.poti.android.domain.type.PartyStatusType

fun ParticipantDetailResponseDto.toDomain(): ParticipantDetail = ParticipantDetail(
    participationId = this.participationId,
    partyId = this.postId,
    orderNumber = this.orderNumber,
    partySummary = PartySummary(
        imageUrl = this.imageUrl,
        artist = this.artistName,
        title = this.title,
        partyStatus = toPartyStatus(this.status),
        statusMessage = this.statusMessage
    ),
    memberPayments = this.memberPayments.map { MemberPayment(
        memberName = it.memberName,
        price = it.price
    ) },
    paymentInfo = PaymentInfo(
        shippingFee = this.paymentInfo.shippingFee,
        totalAmount = this.paymentInfo.totalAmount,
        depositStatus = toDepositStatus(this.paymentInfo.depositStatus),
        bank = this.paymentInfo.bank,
        accountNumber = this.paymentInfo.accountNumber,
        depositDeadline = this.paymentInfo.depositDeadline
    ),
    shippingInfo = ParticipantShippingInfo(
        shippingMethod = this.shippingInfo.shippingMethod,
        receiver = this.shippingInfo.receiver,
        zipcode = this.shippingInfo.zipcode,
        address = this.shippingInfo.address,
        phone = this.shippingInfo.phone,
        carrier = this.shippingInfo.carrier,
        trackingNumber = this.shippingInfo.trackingNumber,
        shippingStatus = toShippingStatus(this.shippingInfo.shippingStatus)
    )
)

private fun toPartyStatus(partyStatus: String): PartyStatusType =
    when(partyStatus) {
        "RECRUITING" -> PartyStatusType.RECRUITING
        "CLOSED" -> PartyStatusType.CLOSED
        "PAYMENT_DONE" -> PartyStatusType.PAYMENT_DONE
        "SHIPPING" -> PartyStatusType.SHIPPING
        "DELIVERED" -> PartyStatusType.DELIVERED

        else -> PartyStatusType.RECRUITING
    }

private fun toDepositStatus(depositStatus: String): ParticipantStatusType =
    when(depositStatus) {
        "WAIT_PAY" -> ParticipantStatusType.WAIT_PAY
        "WAIT_PAY_CHECK" -> ParticipantStatusType.WAIT_PAY_CHECK
        "PAID" -> ParticipantStatusType.PAID
        "READY" -> ParticipantStatusType.READY
        "SHIPPED" -> ParticipantStatusType.SHIPPED
        "DELIVERED" -> ParticipantStatusType.DELIVERED

        else -> ParticipantStatusType.WAIT_PAY
    }

private fun toShippingStatus(shippingStatus: String): ParticipantStatusType =
    when(shippingStatus) {
        "WAIT_PAY" -> ParticipantStatusType.WAIT_PAY
        "WAIT_PAY_CHECK" -> ParticipantStatusType.WAIT_PAY_CHECK
        "PAID" -> ParticipantStatusType.PAID
        "READY" -> ParticipantStatusType.READY
        "SHIPPED" -> ParticipantStatusType.SHIPPED
        "DELIVERED" -> ParticipantStatusType.DELIVERED

        else -> ParticipantStatusType.WAIT_PAY
    }

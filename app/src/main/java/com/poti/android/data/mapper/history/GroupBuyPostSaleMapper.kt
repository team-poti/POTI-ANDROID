package com.poti.android.data.mapper.history

import com.poti.android.data.remote.dto.response.history.GroupBuyPostSaleDto
import com.poti.android.domain.model.history.ParticipantInfo
import com.poti.android.domain.model.history.PartySummary
import com.poti.android.domain.model.history.RecruiterDetail
import com.poti.android.domain.model.history.ShippingInfo
import com.poti.android.domain.type.ParticipantStatusType
import com.poti.android.domain.type.PartyStatusType

fun GroupBuyPostSaleDto.toDomain(): RecruiterDetail = RecruiterDetail(
    recruitId = this.postId,
    orderNumber = this.orderNumber,
    partySummary = PartySummary(
        imageUrl = this.imageUrl,
        artist = this.artistName,
        title = this.title,
        partyStatus = toPartyStatus(this.postStatus),
        statusMessage = this.statusMessage,
    ),
    participantInfoList = this.participant.map {
        ParticipantInfo(
            userId = it.userId,
            memberNames = it.memberNames,
            participantStatus = toParticipantStatus(it.status),
            deliveryMethod = it.priceInfo.shippingName,
            totalPrice = it.priceInfo.totalPrice,
            shippingInfo = ShippingInfo(
                receiverName = it.shippingInfo.receiverName,
                address = it.shippingInfo.address,
                phone = it.shippingInfo.phone,
                trackingNumber = null,
            ),
        )
    },
    participantCount = this.totalCount,
)

private fun toPartyStatus(status: String): PartyStatusType =
    when (status) {
        "RECRUITING" -> PartyStatusType.RECRUITING
        "CLOSED" -> PartyStatusType.CLOSED
        "PAYMENT_DONE" -> PartyStatusType.PAYMENT_DONE
        "SHIPPING" -> PartyStatusType.SHIPPING
        "DELIVERED" -> PartyStatusType.DELIVERED

        else -> PartyStatusType.RECRUITING
    }

private fun toParticipantStatus(status: String): ParticipantStatusType =
    when (status) {
        "RECRUITING" -> ParticipantStatusType.RECRUITING
        "WAIT_PAY" -> ParticipantStatusType.WAIT_PAY
        "WAIT_PAY_CHECK" -> ParticipantStatusType.WAIT_PAY_CHECK
        "PAID" -> ParticipantStatusType.PAID
        "READY" -> ParticipantStatusType.READY
        "SHIPPED" -> ParticipantStatusType.SHIPPED
        "DELIVERED" -> ParticipantStatusType.DELIVERED

        else -> ParticipantStatusType.RECRUITING
    }

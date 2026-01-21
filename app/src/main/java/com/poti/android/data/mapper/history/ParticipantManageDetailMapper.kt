package com.poti.android.data.mapper.history

import com.poti.android.data.remote.dto.response.history.GroupBuyPostParticipantDetailDto
import com.poti.android.domain.model.history.DepositInfo
import com.poti.android.domain.model.history.MemberPriceInfo
import com.poti.android.domain.model.history.ParticipantDetailInfo
import com.poti.android.domain.model.history.ParticipantManageDetail
import com.poti.android.domain.model.history.ShippingInfo
import com.poti.android.domain.type.ParticipantStatusType

fun GroupBuyPostParticipantDetailDto.toDomain(): ParticipantManageDetail = ParticipantManageDetail(
    participants = participants.map {
        ParticipantDetailInfo(
            userId = it.userId,
            profileImage = it.profileImage,
            nickname = it.nickname,
            participantStatus = toParticipantStatus(it.status),
            memberNames = it.memberNames,
            priceInfo = it.priceInfo.memberPerPrices.map { memberPerPriceDto ->
                MemberPriceInfo(
                    name = memberPerPriceDto.name,
                    price = memberPerPriceDto.price,
                )
            },
            shippingName = it.priceInfo.shippingName,
            shippingPrice = it.priceInfo.shippingPrice,
            totalPrice = it.priceInfo.totalPrice,
            depositInfo = it.depositInfo?.run {
                DepositInfo(
                    depositorName = depositorName,
                    depositTime = depositTime,
                )
            },
            shippingInfo = it.shippingInfo?.run {
                ShippingInfo(
                    receiverName = receiverName,
                    address = address,
                    phone = phone,
                    trackingNumber = trackingNumber,
                )
            },
        )
    },
)

private fun toParticipantStatus(status: String): ParticipantStatusType =
    when (status) {
        "RECRUITING" -> ParticipantStatusType.RECRUITING
        "WAIT_PAY" -> ParticipantStatusType.WAIT_PAY
        "WAIT_PAY_CHECK" -> ParticipantStatusType.WAIT_PAY_CHECK
        "PAID" -> ParticipantStatusType.PAID
        "SHIPPED" -> ParticipantStatusType.SHIPPED
        "DELIVERED" -> ParticipantStatusType.DELIVERED

        else -> ParticipantStatusType.RECRUITING
    }

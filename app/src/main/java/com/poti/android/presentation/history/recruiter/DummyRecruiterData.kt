package com.poti.android.presentation.history.recruiter

import com.poti.android.domain.model.history.ParticipantInfo
import com.poti.android.domain.model.history.PartySummary
import com.poti.android.domain.model.history.RecruiterDetail
import com.poti.android.domain.model.history.ShippingInfo
import com.poti.android.domain.type.ParticipantStatusType
import com.poti.android.domain.type.PartyStatusType

val dummyRecruiterData = RecruiterDetail(
    recruitId = 1,
    orderNumber = "모집번호 poti-01",
    partySummary = PartySummary(
        imageUrl = "",
        artist = "ive(아이브)",
        title = "러브다이브 위드뮤",
        partyStatus = PartyStatusType.SHIPPING,
        statusMessage = "배송을 시작했어요",
    ),
    participantInfoList = listOf(
        ParticipantInfo(
            orderId = 1,
            userId = 1,
            memberNames = listOf("레이", "이서"),
            participantStatus = ParticipantStatusType.RECRUITING,
            deliveryMethod = "준등기",
            totalPrice = 12800,
            shippingInfo = ShippingInfo(
                receiverName = "이포티",
                address = "(01234) 서울특별시 솝트구 다솝로 456",
                phone = "010-1234-5678",
                trackingNumber = null,
            ),
        ),
        ParticipantInfo(
            orderId = 2,
            userId = 2,
            memberNames = listOf("레이", "이서"),
            participantStatus = ParticipantStatusType.WAIT_PAY,
            deliveryMethod = "준등기",
            totalPrice = 12800,
            shippingInfo = ShippingInfo(
                receiverName = "이포티",
                address = "(01234) 서울특별시 솝트구 다솝로 456",
                phone = "010-1234-5678",
                trackingNumber = null,
            ),
        ),
        ParticipantInfo(
            orderId = 3,
            userId = 3,
            memberNames = listOf("레이", "이서"),
            participantStatus = ParticipantStatusType.WAIT_PAY_CHECK,
            deliveryMethod = "준등기",
            totalPrice = 12800,
            shippingInfo = ShippingInfo(
                receiverName = "이포티",
                address = "(01234) 서울특별시 솝트구 다솝로 456",
                phone = "010-1234-5678",
                trackingNumber = null,
            ),
        ),
        ParticipantInfo(
            orderId = 4,
            userId = 4,
            memberNames = listOf("레이", "이서"),
            participantStatus = ParticipantStatusType.PAID,
            deliveryMethod = "준등기",
            totalPrice = 12800,
            shippingInfo = ShippingInfo(
                receiverName = "이포티",
                address = "(01234) 서울특별시 솝트구 다솝로 456",
                phone = "010-1234-5678",
                trackingNumber = null,
            ),
        ),
        ParticipantInfo(
            orderId = 5,
            userId = 5,
            memberNames = listOf("레이", "이서"),
            participantStatus = ParticipantStatusType.READY,
            deliveryMethod = "준등기",
            totalPrice = 12800,
            shippingInfo = ShippingInfo(
                receiverName = "이포티",
                address = "(01234) 서울특별시 솝트구 다솝로 456",
                phone = "010-1234-5678",
                trackingNumber = null,
            ),
        ),
        ParticipantInfo(
            orderId = 6,
            userId = 6,
            memberNames = listOf("레이", "이서"),
            participantStatus = ParticipantStatusType.DELIVERED,
            deliveryMethod = "준등기",
            totalPrice = 12800,
            shippingInfo = ShippingInfo(
                receiverName = "이포티",
                address = "(01234) 서울특별시 솝트구 다솝로 456",
                phone = "010-1234-5678",
                trackingNumber = null,
            ),
        ),
    ),
    participantCount = 1,
)

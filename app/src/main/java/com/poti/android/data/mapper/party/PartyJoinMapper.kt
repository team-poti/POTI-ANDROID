package com.poti.android.data.mapper.party

import com.poti.android.data.remote.dto.request.party.DeliveryInfoDto
import com.poti.android.data.remote.dto.request.party.JoinOptionDto
import com.poti.android.data.remote.dto.request.party.PartyJoinRequestDto
import com.poti.android.domain.model.party.PartyJoinInfo

fun PartyJoinInfo.toRequestDto(): PartyJoinRequestDto = PartyJoinRequestDto(
    groupBuyPostId = partyId,
    shippingId = shippingOptionId,
    deliveryInfoDto = DeliveryInfoDto(
        receiverName = deliveryInfo.receiverName,
        zipcode = deliveryInfo.zipcode,
        addressLine = deliveryInfo.address,
        phone = deliveryInfo.phoneNumber,
    ),
    items = joinItems.map { item ->
        JoinOptionDto(
            groupBuyOptionId = item.optionId,
            count = item.count,
        )
    },
)

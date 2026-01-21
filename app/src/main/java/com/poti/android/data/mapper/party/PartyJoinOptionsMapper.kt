package com.poti.android.data.mapper.party

import com.poti.android.data.remote.dto.response.party.DeliveryOptionDto
import com.poti.android.data.remote.dto.response.party.MemberOptionDto
import com.poti.android.data.remote.dto.response.party.PartyJoinOptionsDto
import com.poti.android.domain.model.delivery.DeliveryOption
import com.poti.android.domain.model.party.Members
import com.poti.android.domain.model.party.PartyJoinOption

fun PartyJoinOptionsDto.toDomain(): PartyJoinOption = PartyJoinOption(
    memberOptions = members.map { it.toDomain() },
    deliveryOptions = shippings.map { it.toDomain() },
)

fun MemberOptionDto.toDomain(): Members = Members(
    memberId = memberOptionId,
    memberName = memberName,
    memberPrice = memberOptionPrice,
)

fun DeliveryOptionDto.toDomain(): DeliveryOption = DeliveryOption(
    deliveryId = deliveryOptionId,
    name = deliveryName,
    price = deliveryOptionPrice,
)

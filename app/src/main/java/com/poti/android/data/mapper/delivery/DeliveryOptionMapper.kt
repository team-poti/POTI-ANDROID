package com.poti.android.data.mapper.delivery

import com.poti.android.data.remote.dto.request.party.ShippingRequestDto
import com.poti.android.data.remote.dto.response.party.ShippingOptionResponseDto
import com.poti.android.domain.model.delivery.DeliveryOption

fun ShippingOptionResponseDto.toDomain(): DeliveryOption =
    DeliveryOption(
        deliveryId = this.deliveryId,
        name = this.name,
        price = this.price,
    )

fun DeliveryOption.toDto(): ShippingRequestDto =
    ShippingRequestDto(
        deliveryMethodId = this.deliveryId,
        price = this.price,
    )

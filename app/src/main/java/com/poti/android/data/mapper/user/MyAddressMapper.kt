package com.poti.android.data.mapper.user

import com.poti.android.data.remote.dto.request.user.MyAddressRequestDto
import com.poti.android.data.remote.dto.response.user.MyAddressResponseDto
import com.poti.android.domain.model.delivery.DeliveryInfo

fun MyAddressResponseDto.toDomain(): DeliveryInfo =
    DeliveryInfo(
        receiverName = receiverName,
        zipcode = zipcode,
        address = address,
        addressDetail = addressDetail,
        phoneNumber = phone,
    )

fun DeliveryInfo.toRequestDto(): MyAddressRequestDto =
    MyAddressRequestDto(
        receiverName = receiverName,
        zipcode = zipcode,
        address = address,
        addressDetail = addressDetail,
        phone = phoneNumber,
    )

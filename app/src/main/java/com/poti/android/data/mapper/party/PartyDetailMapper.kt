package com.poti.android.data.mapper.party

import com.poti.android.data.remote.dto.response.party.ImageResponseDto
import com.poti.android.data.remote.dto.response.party.ParticipantDto
import com.poti.android.data.remote.dto.response.party.PartyDetailResponseDto
import com.poti.android.data.remote.dto.response.party.ShippingOptionDto
import com.poti.android.data.remote.dto.response.party.UploaderDto
import com.poti.android.domain.model.delivery.DeliveryOption
import com.poti.android.domain.model.party.Participant
import com.poti.android.domain.model.party.PartyDetail
import com.poti.android.domain.model.party.PartyImage
import com.poti.android.domain.model.user.UserSummary
import com.poti.android.domain.type.PartyStatusType

fun PartyDetailResponseDto.toDomain(): PartyDetail = PartyDetail(
    postId = postId,
    isMyPost = isMyPost,
    isParticipated = isParticipated,
    status = mapToPartyStatus(status),
    artist = artist,
    artistId = artistId,
    title = title,
    price = price,
    uploadTime = uploadTime,
    deadline = deadline,
    images = images.map { it.toDomain() },
    content = content,
    deliveryOptions = shippingOptionDtos.map { it.toDomain() },
    uploader = uploaderDto.toDomain(),
    currentCount = currentCount,
    totalCount = totalCount,
    participants = participants.map { it.toDomain() },
)

fun ImageResponseDto.toDomain(): PartyImage = PartyImage(
    sortOrder = order,
    imageUrl = url,
)

fun ShippingOptionDto.toDomain(): DeliveryOption = DeliveryOption(
    deliveryId = shippingId,
    name = name,
    price = price,
)

fun UploaderDto.toDomain(): UserSummary = UserSummary(
    userId = userId,
    nickname = nickname,
    profileImage = profileImage,
    rating = rating,
    reviewCount = reviewCount,
)

fun ParticipantDto.toDomain(): Participant = Participant(
    userId = userId,
    nickname = nickname,
    profileImage = profileImage,
    rating = rating,
    selectedMembers = selectedMembers,
)

fun mapToPartyStatus(string: String): PartyStatusType = try {
    PartyStatusType.valueOf(string)
} catch (e: IllegalArgumentException) {
    PartyStatusType.CLOSED
}

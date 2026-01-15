package com.poti.android.domain.model.party

data class PartyImage(
    val sortOrder: Int, // 이미지 노출 순서
    val imageUrl: String, // 이미지 S3 URL
)

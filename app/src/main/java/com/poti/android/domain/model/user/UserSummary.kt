package com.poti.android.domain.model.user

data class UserSummary(
    val userId: Long, // 총대 유저 ID
    val nickname: String, // 총대 닉네임
    val profileImage: String?, // 총대 프로필 이미지 (없으면 null)
    val rating: Double, // 총대 매너 온도/평점
    val reviewCount: Int, // 총대가 받은 거래 후기 수
)

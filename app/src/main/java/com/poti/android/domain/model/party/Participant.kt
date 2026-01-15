package com.poti.android.domain.model.party

data class Participant(
    val userId: Long, // 참여자 유저 ID
    val nickname: String, // 참여자 닉네임
    val profileImage: String?, // 참여자 프로필 이미지 (없으면 null)
    val rating: Double, // 참여자 평점
    val selectedMembers: List<String>, // 선점한 멤버 목록
)

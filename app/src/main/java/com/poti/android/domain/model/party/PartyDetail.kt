package com.poti.android.domain.model.party

import com.poti.android.domain.model.delivery.DeliveryOption
import com.poti.android.domain.model.user.UserSummary
import com.poti.android.domain.type.PartyStatusType

data class PartyDetail(
    val postId: Long, // 분철글 고유 ID
    val isMyPost: Boolean, // 본인 작성 글 여부
    val isParticipated: Boolean, // 본인 참여 여부
    val status: PartyStatusType, // 모집 상태
    val artist: String, // 아티스트 그룹명
    val artistId: Long, // 아티스트 아이디
    val title: String, // 분철글 제목
    val price: Int, // 1인당 가격 (원)
    val uploadTime: String, // 업로드 시간 (예: "2026-01-22T06:21:20.697608")
    val deadline: String, // 모집 마감일
    val images: List<PartyImage>, // 상품 이미지 리스트
    val content: String, // 분철글 본문 내용
    val deliveryOptions: List<DeliveryOption>, // 배송 방법 리스트
    val uploader: UserSummary, // 총대(작성자) 정보
    val currentCount: Int, // 현재 참여 인원 수
    val totalCount: Int, // 총 모집 인원 수
    val participants: List<Participant>, // 참여자 정보 리스트
)

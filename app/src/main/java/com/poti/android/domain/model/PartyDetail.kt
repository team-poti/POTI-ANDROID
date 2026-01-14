package com.poti.android.domain.model

import com.poti.android.domain.type.PartyStatusType

data class PartyDetail(
    val postId: Long, // 분철글 고유 ID
    val isMyPost: Boolean, // 본인 작성 글 여부
    val isLiked: Boolean, // 좋아요 여부
    val status: PartyStatusType, // 모집 상태
    val artist: String, // 아티스트 그룹명
    val title: String, // 분철글 제목
    val price: Int, // 1인당 가격 (원)
    val uploadTime: String, // 업로드 시간 (예: "4시간 전")
    val deadline: String, // 모집 마감일
    val content: String, // 분철글 본문 내용
    val images: List<PartyImage>, // 상품 이미지 리스트
    val shippingOptions: List<ShippingOption>, // 배송 방법 리스트
    val uploader: Uploader, // 총대9작성자) 정보
    val participants: List<Participant>, // 참여자 정보 리스트
    val currentCount: Int, // 현재 참여 인원 수
    val totalCount: Int, // 총 모집 인원 수
)

data class PartyImage(
    val order: Int, // 이미지 노출 순서
    val url: String, // 이미지 S3 URL
)

data class ShippingOption(
    val name: String, // 배송 방식
    val price: Int, // 배송비
)

data class Uploader(
    val userId: Long, // 총대 유저 ID
    val nickname: String, // 총대 닉네임
    val profileImage: String?, // 총대 프로필 이미지 (없으면 null)
    val rating: Double, // 총대 매너 온도/평점
    val reviewCount: Int, // 총대가 받은 거래 후기 수
)

data class Participant(
    val userId: Long, // 참여자 유저 ID
    val nickname: String, // 참여자 닉네임
    val profileImage: String?, // 참여자 프로필 이미지 (없으면 null)
    val rating: Double, // 참여자 평점
    val selectedMembers: List<String>, // 선점한 멤버 목록
)

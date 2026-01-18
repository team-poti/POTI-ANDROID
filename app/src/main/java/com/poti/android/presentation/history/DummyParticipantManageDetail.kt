package com.poti.android.presentation.history

import com.poti.android.domain.model.history.*
import com.poti.android.domain.type.ParticipantStatusType

object DummyParticipantManageDetail {
    // ========================================================================
    // 1. Shared Common Data (공통 더미 데이터)
    // ========================================================================

    private val commonArtistInfo = ArtistInfo(
        artist = "NewJeans",
        title = "How Sweet 위버스 특전 분철",
        imageUrl = "https://picsum.photos/200",
        partyState = ParticipantStatusType.RECRUIT_DONE,
    )

    private val commonDepositItems = listOf(
        DepositItem.MemberItem(name = "해린 포토카드", price = 15000),
        DepositItem.DeliveryItem(name = "GS25 반값택배", price = 1800),
    )

    private val commonShippingInfo = ParticipantShippingInfo(
        recipient = "김포티",
        zipcode = "06000",
        address = "서울특별시 강남구 테헤란로 123 포티타워 101호",
        phone = "010-1234-5678",
        deliveryMethod = "GS25 반값택배",
        trackingNumber = null,
    )

    // ========================================================================
    // 2. Recruiter View Data (총대 관리 페이지용 - RecruiterDetail)
    // ========================================================================

    // Step 0: 모집 중/완료 단계
    val recruiterRecruitStep = RecruiterDetail(
        recruitId = 1,
        artistInfo = ArtistInfo(
            imageUrl = "",
            artist = "IVE(아이브)",
            title = "I've IVE 위드뮤 분철",
            partyState = ParticipantStatusType.RECRUIT_WAIT,
        ),
        progressInfo = ProgressInfo(
            guideText = "모집이 시작되었습니다.",
            step = 0,
        ),
        participantInfoList = listOf(
            ParticipantInfo(
                userId = 101,
                memberNames = "장원영",
                participantState = ParticipantStatusType.RECRUIT_DONE,
                userInfo = "포티",
                deliveryMethod = "GS반값택배",
                deliveryPrice = 1800,
            ),
        ),
    )

    // Step 1: 입금 진행/확인 단계
    val recruiterDepositStep = RecruiterDetail(
        recruitId = 2,
        artistInfo = ArtistInfo(
            imageUrl = "",
            artist = "aespa(에스파)",
            title = "Armageddon 미공포 분철",
            partyState = ParticipantStatusType.DEPOSIT_WAIT,
        ),
        progressInfo = ProgressInfo(
            guideText = "입금을 확인하고 있습니다.",
            step = 1,
        ),
        participantInfoList = listOf(
            ParticipantInfo(
                userId = 201,
                memberNames = "카리나, 윈터",
                participantState = ParticipantStatusType.DEPOSIT_DONE,
                userInfo = "김철수",
                deliveryMethod = "CU끼리택배",
                deliveryPrice = 1600,
            ),
            ParticipantInfo(
                userId = 202,
                memberNames = "닝닝",
                participantState = ParticipantStatusType.DEPOSIT_WAIT,
                userInfo = "이영희",
                deliveryMethod = "준등기",
                deliveryPrice = 1800,
            ),
            ParticipantInfo(
                userId = 203,
                memberNames = "지젤",
                participantState = ParticipantStatusType.DEPOSIT_CHECK,
                userInfo = "박민수",
                deliveryMethod = "일반택배",
                deliveryPrice = 3500,
            ),
        ),
    )

    // Step 2: 배송/종료 단계
    val recruiterDeliveryDoneStep = RecruiterDetail(
        recruitId = 3,
        artistInfo = ArtistInfo(
            imageUrl = "",
            artist = "NewJeans",
            title = "How Sweet 위버스 특전",
            partyState = ParticipantStatusType.DELIVERY_DONE,
        ),
        progressInfo = ProgressInfo(
            guideText = "모든 진행이 완료되었습니다.",
            step = 2,
        ),
        participantInfoList = listOf(
            ParticipantInfo(
                userId = 301,
                memberNames = "민지, 하니",
                participantState = ParticipantStatusType.DELIVERY_DONE,
                userInfo = "최예나",
                deliveryMethod = "GS반값택배",
                deliveryPrice = 1800,
            ),
            ParticipantInfo(
                userId = 302,
                memberNames = "다니엘",
                participantState = ParticipantStatusType.DELIVERY_DONE,
                userInfo = "조유리",
                deliveryMethod = "GS반값택배",
                deliveryPrice = 1800,
            ),
        ),
    )

    // ========================================================================
    // 3. Participant View Data (참여자 상세 페이지용 - ParticipantDetail)
    // ========================================================================

    /**
     * Case 1: 입금 대기 (Recruit Done -> Deposit Wait)
     * - 액션: 하단 [입금 완료] 버튼 노출
     */
    val participantDetailWaitDeposit = ParticipantDetail(
        recruitId = 1001L,
        artistInfo = commonArtistInfo,
        progressInfo = ProgressInfo(
            guideText = "입금을 진행해주세요.",
            step = 1,
        ),
        depositInfo = ParticipantDepositInfo(
            items = commonDepositItems,
            totalAmount = 16800,
            depositStatus = DepositStatus.DepositWait(
                accountNumber = "카카오뱅크 3333-01-1234567",
                dueDate = "2024.12.31 23:59까지",
            ),
        ),
        shippingInfo = commonShippingInfo,
        userState = ParticipantStatusType.RECRUIT_DONE,
        // [추가된 필드]
        recruiterName = "포티 총대",
        recruiterProfileUrl = "https://picsum.photos/id/64/200",
        recruiterRating = "4.8"
    )

    /**
     * Case 2: 입금 확인 중 (Deposit Check)
     * - 액션: 버튼 없음 (총대 확인 대기)
     */
    val participantDetailCheckDeposit = ParticipantDetail(
        recruitId = 1002L,
        artistInfo = commonArtistInfo,
        progressInfo = ProgressInfo(
            guideText = "총대가 입금을 확인하고 있습니다.",
            step = 1,
        ),
        depositInfo = ParticipantDepositInfo(
            items = commonDepositItems,
            totalAmount = 16800,
            depositStatus = DepositStatus.DepositCheck(
                accountNumber = "카카오뱅크 3333-01-1234567",
                dueDate = "2024.12.31 23:59까지",
            ),
        ),
        shippingInfo = commonShippingInfo,
        userState = ParticipantStatusType.DEPOSIT_CHECK,
        // [추가된 필드]
        recruiterName = "포티 총대",
        recruiterProfileUrl = "https://picsum.photos/id/64/200",
        recruiterRating = "4.8"
    )

    /**
     * Case 3: 배송 시작 (Delivery Start)
     * - 액션: 하단 [수령 완료] 버튼 노출
     * - 특징: 운송장 번호 존재
     */
    val participantDetailDeliveryStart = ParticipantDetail(
        recruitId = 1003L,
        artistInfo = commonArtistInfo,
        progressInfo = ProgressInfo(
            guideText = "배송이 시작되었습니다.",
            step = 2,
        ),
        depositInfo = ParticipantDepositInfo(
            items = commonDepositItems,
            totalAmount = 16800,
            depositStatus = DepositStatus.DepositDone,
        ),
        shippingInfo = commonShippingInfo.copy(
            trackingNumber = "1234-5678-9012",
        ),
        userState = ParticipantStatusType.DELIVERY_START,
        // [추가된 필드]
        recruiterName = "포티 총대",
        recruiterProfileUrl = "https://picsum.photos/id/64/200",
        recruiterRating = "4.8"
    )
}

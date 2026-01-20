package com.poti.android.presentation.history

import com.poti.android.domain.model.history.ArtistInfo
import com.poti.android.domain.model.history.DepositItem
import com.poti.android.domain.model.history.ParticipantDepositInfo
import com.poti.android.domain.model.history.ParticipantDetail
import com.poti.android.domain.model.history.ParticipantInfo
import com.poti.android.domain.model.history.ParticipantShippingInfo
import com.poti.android.domain.model.history.ProgressInfo
import com.poti.android.domain.model.history.RecruiterDetail
import com.poti.android.domain.type.ParticipantStatusType

object DummyParticipantManageDetail {
    // HistorySections
    val dummyArtistInfo = ArtistInfo(
        imageUrl = "",
        artist = "ive(아이브)",
        title = "러브다이브 위드뮤",
        partyState = com.poti.android.domain.type.ParticipantStatusType.RECRUIT_DONE,
    )

    val dummyProgressInfoStep0 = ProgressInfo(
        guideText = "참여자들을 기다리고 있어요",
        step = 0,
    )

    val dummyProgressInfoStep2 = ProgressInfo(
        guideText = "입금을 확인하고 있습니다.",
        step = 2,
    )
    val recruitStep = RecruiterDetail(
        partyId = 1,
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

    val depositStep = RecruiterDetail(
        partyId = 2,
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

    val deliveryDoneStep = RecruiterDetail(
        partyId = 3,
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

    val dummyParticipantDetail = ParticipantDetail(
        partyId = 12345L,
        artistInfo = ArtistInfo(
            artist = "뉴진스",
            title = "NewJeans",
            imageUrl = "https://example.com/image.jpg",
            partyState = ParticipantStatusType.DEPOSIT_DONE,
        ),
        progressInfo = ProgressInfo(
            guideText = "모집이 시작되었습니다.",
            step = 2,
        ),
        depositInfo = ParticipantDepositInfo(
            items = listOf(
                DepositItem.MemberItem(
                    name = "해린 포토카드",
                    price = 5000,
                ),
                DepositItem.DeliveryItem(
                    name = "준등기",
                    price = 1800,
                ),
            ),
            totalAmount = 6800,
        ),
        shippingInfo = ParticipantShippingInfo(
            recipient = "김포티",
            zipcode = "06000",
            address = "서울특별시 강남구 테헤란로 123 포티타워 101호",
            phone = "010-1234-5678",
            deliveryMethod = "준등기",
            trackingNumber = "123456789012",
        ),
        userState = ParticipantStatusType.RECRUIT_WAIT,
    )
}

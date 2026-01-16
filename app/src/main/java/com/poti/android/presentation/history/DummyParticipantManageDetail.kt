package com.poti.android.presentation.history

import com.poti.android.domain.model.history.ArtistInfo
import com.poti.android.domain.model.history.ParticipantInfo
import com.poti.android.domain.model.history.RecruiterDetail
import com.poti.android.domain.model.history.ProgressInfo
import com.poti.android.domain.type.ParticipantStatusType

object DummyParticipantManageDetail {
    val recruitStep = RecruiterDetail(
        partyId = 1,
        artistInfo = ArtistInfo(
            imageUrl = "",
            artist = "IVE(아이브)",
            title = "I've IVE 위드뮤 분철",
            partyState = ParticipantStatusType.RECRUIT_WAIT
        ),
        progressInfo = ProgressInfo(
            guideText = "모집이 시작되었습니다.",
            step = 0
        ),
        participantInfoList = listOf(
            ParticipantInfo(
                userId = 101,
                memberNames = "장원영",
                participantState = ParticipantStatusType.RECRUIT_DONE,
                userInfo = "포티",
                deliveryMethod = "GS반값택배",
                deliveryPrice = 1800
            )
        )
    )

    val depositStep = RecruiterDetail(
        partyId = 2,
        artistInfo = ArtistInfo(
            imageUrl = "",
            artist = "aespa(에스파)",
            title = "Armageddon 미공포 분철",
            partyState = ParticipantStatusType.DEPOSIT_WAIT
        ),
        progressInfo = ProgressInfo(
            guideText = "입금을 확인하고 있습니다.",
            step = 1
        ),
        participantInfoList = listOf(
            ParticipantInfo(
                userId = 201,
                memberNames = "카리나, 윈터",
                participantState = ParticipantStatusType.DEPOSIT_DONE,
                userInfo = "김철수",
                deliveryMethod = "CU끼리택배",
                deliveryPrice = 1600
            ),
            ParticipantInfo(
                userId = 202,
                memberNames = "닝닝",
                participantState = ParticipantStatusType.DEPOSIT_WAIT,
                userInfo = "이영희",
                deliveryMethod = "준등기",
                deliveryPrice = 1800
            ),
            ParticipantInfo(
                userId = 203,
                memberNames = "지젤",
                participantState = ParticipantStatusType.DEPOSIT_CHECK,
                userInfo = "박민수",
                deliveryMethod = "일반택배",
                deliveryPrice = 3500
            )
        )
    )

    val deliveryDoneStep = RecruiterDetail(
        partyId = 3,
        artistInfo = ArtistInfo(
            imageUrl = "",
            artist = "NewJeans",
            title = "How Sweet 위버스 특전",
            partyState = ParticipantStatusType.DELIVERY_DONE
        ),
        progressInfo = ProgressInfo(
            guideText = "모든 진행이 완료되었습니다.",
            step = 2
        ),
        participantInfoList = listOf(
            ParticipantInfo(
                userId = 301,
                memberNames = "민지, 하니",
                participantState = ParticipantStatusType.DELIVERY_DONE,
                userInfo = "최예나",
                deliveryMethod = "GS반값택배",
                deliveryPrice = 1800
            ),
            ParticipantInfo(
                userId = 302,
                memberNames = "다니엘",
                participantState = ParticipantStatusType.DELIVERY_DONE,
                userInfo = "조유리",
                deliveryMethod = "GS반값택배",
                deliveryPrice = 1800
            )
        )
    )
}

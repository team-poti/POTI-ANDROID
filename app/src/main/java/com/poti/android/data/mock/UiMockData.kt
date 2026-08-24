package com.poti.android.data.mock

import com.poti.android.domain.model.artist.Artist
import com.poti.android.domain.model.artist.ArtistSearchResult
import com.poti.android.domain.model.artist.Member
import com.poti.android.domain.model.artist.MemberPriceOption
import com.poti.android.domain.model.auth.SocialType
import com.poti.android.domain.model.auth.UserAuth
import com.poti.android.domain.model.delivery.DeliveryOption
import com.poti.android.domain.model.history.DeliveryDetail
import com.poti.android.domain.model.history.DepositInfo
import com.poti.android.domain.model.history.MemberPayment
import com.poti.android.domain.model.history.MemberPriceInfo
import com.poti.android.domain.model.history.MyParty
import com.poti.android.domain.model.history.MyPartyList
import com.poti.android.domain.model.history.ParticipantDetail
import com.poti.android.domain.model.history.ParticipantDetailInfo
import com.poti.android.domain.model.history.ParticipantInfo
import com.poti.android.domain.model.history.ParticipantManageDetail
import com.poti.android.domain.model.history.ParticipantPaymentConfirm
import com.poti.android.domain.model.history.ParticipantShippingInfo
import com.poti.android.domain.model.history.PartySummary
import com.poti.android.domain.model.history.PaymentInfo
import com.poti.android.domain.model.history.RecruiterDetail
import com.poti.android.domain.model.history.ShippingInfo
import com.poti.android.domain.model.home.Banner
import com.poti.android.domain.model.home.GroupItem
import com.poti.android.domain.model.home.HomeContent
import com.poti.android.domain.model.image.PresignedUploadInfo
import com.poti.android.domain.model.party.Members
import com.poti.android.domain.model.party.Participant
import com.poti.android.domain.model.party.PartyDetail
import com.poti.android.domain.model.party.PartyImage
import com.poti.android.domain.model.party.PartyJoinOption
import com.poti.android.domain.model.party.ProductCategory
import com.poti.android.domain.model.party.ProductPartyList
import com.poti.android.domain.model.payment.PaymentResult
import com.poti.android.domain.model.user.HistorySummary
import com.poti.android.domain.model.user.UserAccount
import com.poti.android.domain.model.user.UserMyPage
import com.poti.android.domain.model.user.UserProfile
import com.poti.android.domain.model.user.UserSummary
import com.poti.android.domain.type.HistoryListType
import com.poti.android.domain.type.ParticipantStatusType
import com.poti.android.domain.type.PartyStatusType
import com.poti.android.domain.model.party.GroupItem as PartyGroupItem
import com.poti.android.domain.model.party.PartySummary as PartyListSummary

object UiMockData {
    private val historyPartySummary = PartySummary(
        imageUrl = "",
        artist = "IVE",
        title = "러브다이브 위드뮤",
        partyStatus = PartyStatusType.SHIPPING,
        statusMessage = "배송을 시작했어요",
    )

    private val shippingInfo = ShippingInfo(
        receiverName = "이포티",
        address = "(01234) 서울특별시 포티구",
        phone = "010-1234-5678",
        trackingNumber = null,
    )

    val artists = listOf(
        Artist(1, "IVE", ""),
        Artist(2, "aespa", ""),
        Artist(3, "LE SSERAFIM", ""),
        Artist(4, "NewJeans", ""),
        Artist(5, "NMIXX", ""),
        Artist(6, "아이유", ""),
    )

    val members = listOf(
        Member(1, "안유진"),
        Member(2, "가을"),
        Member(3, "레이"),
        Member(4, "장원영"),
        Member(5, "리즈"),
        Member(6, "이서"),
    )

    val memberPriceOptions = members.mapIndexed { index, member ->
        MemberPriceOption(
            memberId = member.memberId,
            name = member.name,
            price = (5_000 + index * 500).toString(),
        )
    }

    val artistSearchResults = artists.map { ArtistSearchResult(it.artistId, it.name) }

    val deliveryOptions = listOf(
        DeliveryOption(1, "준등기", 1_800),
        DeliveryOption(2, "일반 택배", 4_000),
    )

    val homeGroupItems = listOf(
        GroupItem("2026 시즌 콘서트 후드", "아이유", 6, "", 3, "인기"),
        GroupItem("공식 응원봉 Ver.2", "IVE", 1, "", 12, "NEW"),
        GroupItem("월드투어 포토북", "aespa", 2, "", 7, ""),
    )

    val homeContent = HomeContent(
        nickname = "포티",
        mainArtist = "IVE",
        mainArtistId = 1,
        myGroupItems = homeGroupItems,
        otherGroupItems = homeGroupItems.reversed(),
        banners = listOf(Banner(1, ""), Banner(2, ""), Banner(3, "")),
    )

    val productCategory = ProductCategory(
        nickname = "포티",
        mainArtist = "IVE",
        mainArtistId = 1,
        groupItems = List(24) { index ->
            val titles = listOf(
                "공식 응원봉",
                "콘서트 MD 세트",
                "포토카드 랜덤팩",
                "시즌그리팅",
                "월드투어 후드",
                "앨범 럭키드로우",
            )
            val tags = listOf("인기", "NEW", null, "마감임박")

            PartyGroupItem(
                artist = if (index % 2 == 0) "IVE" else "aespa",
                artistId = if (index % 2 == 0) 1 else 2,
                postImage = "",
                postTitle = "${titles[index % titles.size]} ${index + 1}",
                postCount = (index % 9) + 1,
                tag = tags[index % tags.size],
            )
        },
    )

    val productPartyList = ProductPartyList(
        partyTitle = "러브다이브 위드뮤",
        artistName = "IVE",
        partySummaries = List(24) { index ->
            val partyId = index + 1L
            val availableMembers = when (index % 4) {
                0 -> listOf("원영", "유진")
                1 -> listOf("레이", "이서")
                2 -> listOf("가을", "리즈")
                else -> listOf("안유진", "장원영", "리즈")
            }

            PartyListSummary(
                partyId = partyId,
                price = 18_000 + (index % 5) * 1_000,
                productImageUrl = "",
                currentCount = (index % 5) + 1,
                totalCount = 6,
                availableMembers = availableMembers,
                profileImageUrl = "",
                nickname = "분철러${index + 1}",
                rating = 4.0 + (index % 10) / 10.0,
            )
        },
    )

    val partyDetail = PartyDetail(
        postId = 1,
        isMyPost = false,
        status = PartyStatusType.RECRUITING,
        artist = "IVE",
        artistId = 1,
        title = "러브다이브 위드뮤",
        price = 5_000,
        uploadTime = "4시간 전",
        deadline = "2026-12-31",
        content = "UI 개발용 분철 상세 mock 데이터입니다.",
        images = listOf(PartyImage(1, ""), PartyImage(2, "")),
        deliveryOptions = deliveryOptions,
        uploader = UserSummary(1, "포티공주", null, 4.8, 14),
        participants = listOf(
            Participant(2, "참여자1", null, 4.5, listOf("원영")),
            Participant(3, "참여자2", null, 4.7, listOf("유진")),
        ),
        currentCount = 2,
        totalCount = 6,
    )

    val partyJoinOption = PartyJoinOption(
        memberOptions = memberPriceOptions.map {
            Members(it.memberId, it.name, it.price.toInt())
        },
        deliveryOptions = deliveryOptions,
    )

    fun myPartyList(
        status: String,
        participation: Boolean,
    ): MyPartyList {
        val state = runCatching { HistoryListType.valueOf(status) }
            .getOrDefault(HistoryListType.IN_PROGRESS)
        return MyPartyList(
            currentState = state,
            inProgressCount = 2,
            completedCount = 1,
            partyList = listOf(
                MyParty(
                    participationId = if (participation) 101 else null,
                    groupBuyId = 1,
                    artistName = "IVE",
                    productName = "러브다이브 위드뮤",
                    thumbnailUrl = null,
                    postStatus = if (state == HistoryListType.COMPLETED) {
                        PartyStatusType.COMPLETED
                    } else {
                        PartyStatusType.RECRUITING
                    },
                ),
                MyParty(
                    participationId = if (participation) 102 else null,
                    groupBuyId = 2,
                    artistName = "aespa",
                    productName = "공식 응원봉",
                    thumbnailUrl = null,
                    postStatus = PartyStatusType.SHIPPING,
                ),
            ),
        )
    }

    val recruiterDetail = RecruiterDetail(
        recruitId = 1,
        orderNumber = "모집번호 POTI-01",
        partySummary = historyPartySummary,
        participantInfoList = listOf(
            ParticipantInfo(
                orderId = 1,
                userId = 2,
                memberNames = listOf("레이", "이서"),
                participantStatus = ParticipantStatusType.WAIT_PAY_CHECK,
                deliveryMethod = "준등기",
                totalPrice = 12_800,
                shippingInfo = shippingInfo,
            ),
            ParticipantInfo(
                orderId = 2,
                userId = 3,
                memberNames = listOf("원영"),
                participantStatus = ParticipantStatusType.READY,
                deliveryMethod = "일반 택배",
                totalPrice = 9_000,
                shippingInfo = shippingInfo,
            ),
        ),
        participantCount = 2,
    )

    val participantManageDetail = ParticipantManageDetail(
        participants = listOf(
            ParticipantDetailInfo(
                orderId = 1,
                userId = 2,
                profileImage = null,
                nickname = "참여자1",
                participantStatus = ParticipantStatusType.WAIT_PAY_CHECK,
                memberNames = listOf("레이", "이서"),
                priceInfo = listOf(MemberPriceInfo("레이", 5_000), MemberPriceInfo("이서", 5_000)),
                shippingName = "준등기",
                shippingPrice = 1_800,
                totalPrice = 11_800,
                depositInfo = DepositInfo("이포티", "2026-06-06T12:00:00"),
                shippingInfo = shippingInfo,
            ),
        ),
    )

    val participantDetail = ParticipantDetail(
        participationId = 101,
        partyId = 1,
        orderNumber = "참여번호 POTI-101",
        partySummary = historyPartySummary,
        memberPayments = listOf(MemberPayment("레이", 5_000), MemberPayment("이서", 5_000)),
        paymentInfo = PaymentInfo(
            shippingFee = 1_800,
            totalAmount = 11_800,
            depositStatus = ParticipantStatusType.WAIT_PAY,
            bank = "신한은행",
            accountNumber = "110-123-456789",
            depositDeadline = "2026-12-31",
        ),
        shippingInfo = ParticipantShippingInfo(
            shippingMethod = "준등기",
            receiver = "이포티",
            zipcode = "01234",
            address = "서울특별시 포티구",
            phone = "010-1234-5678",
            carrier = null,
            trackingNumber = null,
            shippingStatus = ParticipantStatusType.READY,
        ),
    )

    val userProfile = UserProfile(
        userId = 1,
        nickname = "포티공주",
        profileImageUrl = "",
        ratingAvg = 4.8,
        activityMessage = "안전하고 즐거운 분철해요.",
        joinedAt = "2026.01",
        participationSummary = HistorySummary(2, 6),
        recruitSummary = HistorySummary(2, 10),
    )

    val userMyPage = UserMyPage(
        nickname = userProfile.nickname,
        email = "poti@example.com",
        profileImageUrl = null,
        ratingAvg = userProfile.ratingAvg.toString(),
        activityMessage = userProfile.activityMessage,
        joinedAt = userProfile.joinedAt,
        hasFavoriteArtist = true,
        favoriteArtistName = "IVE",
        participationSummary = userProfile.participationSummary,
        recruitSummary = userProfile.recruitSummary,
    )

    val userAccount = UserAccount(
        nickname = userProfile.nickname,
        email = userProfile.email,
        socialType = SocialType.KAKAO,
    )

    val userAuth = UserAuth(
        accessToken = "ui-mock-access-token",
        refreshToken = "ui-mock-refresh-token",
        isNewUser = false,
        userId = 1,
    )

    fun presignedUploadInfos(extensions: List<String>) = extensions.mapIndexed { index, extension ->
        PresignedUploadInfo(
            fileName = "ui-mock-image-$index.$extension",
            url = "https://example.invalid/ui-mock-image-$index.$extension",
        )
    }

    fun paymentResult(orderId: Long) = PaymentResult(paymentId = orderId)

    fun paymentConfirm(orderId: Long) = ParticipantPaymentConfirm(
        orderId = orderId,
        orderStatus = "PAID",
        confirmedAt = "2026-06-06T12:00:00",
    )

    fun deliveryDetail(
        orderId: Long,
        trackingNumber: String,
    ) = DeliveryDetail(
        orderId = orderId,
        deliveryStatus = "SHIPPED",
        trackingNumber = trackingNumber,
        shippedAt = "2026-06-06T12:00:00",
    )
}

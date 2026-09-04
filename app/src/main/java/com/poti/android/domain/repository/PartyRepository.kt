package com.poti.android.domain.repository

import com.poti.android.domain.model.artist.ArtistSearchResult
import com.poti.android.domain.model.artist.MemberPriceOption
import com.poti.android.domain.model.delivery.DeliveryOption
import com.poti.android.domain.model.history.MyPartyList
import com.poti.android.domain.model.history.ParticipantManageDetail
import com.poti.android.domain.model.history.RecruiterDetail
import com.poti.android.domain.model.party.PartyDetail
import com.poti.android.domain.model.party.PartyJoinInfo
import com.poti.android.domain.model.party.PartyJoinOption
import com.poti.android.domain.model.party.ProductPartyList

interface PartyRepository {
    suspend fun searchProductTitle(
        artistId: Long,
        keyword: String,
    ): Result<List<String>>

    suspend fun searchArtist(
        keyword: String,
    ): Result<List<ArtistSearchResult>>

    suspend fun createPost(
        artistId: Long,
        product: String,
        description: String,
        deadline: String,
        bank: String,
        accountNumber: String,
        imageUrls: List<String>,
        options: List<MemberPriceOption>,
        shippings: List<DeliveryOption>,
    ): Result<Long>

    suspend fun getShippingOptions(): Result<List<DeliveryOption>>

    suspend fun getPartyDetail(partyId: Long): Result<PartyDetail>

    suspend fun getPartyJoinOptions(partyId: Long): Result<PartyJoinOption>

    suspend fun postPartyJoin(joinInfo: PartyJoinInfo): Result<Long>

    suspend fun getMyRecruitList(status: String): Result<MyPartyList>

    suspend fun getRecruitDetail(postId: Long): Result<RecruiterDetail>

    suspend fun getRecruitPostParticipant(postId: Long): Result<ParticipantManageDetail>

    suspend fun deleteRecruitPost(postId: Long): Result<Unit>

    suspend fun getProductPartyList(
        page: Int?,
        size: Int?,
        title: String,
        artistId: Long,
        sort: String,
        memberIds: List<Long>?,
    ): Result<ProductPartyList>
}

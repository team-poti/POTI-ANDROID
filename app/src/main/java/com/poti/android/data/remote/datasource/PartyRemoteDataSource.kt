package com.poti.android.data.remote.datasource

import com.poti.android.core.network.model.BaseResponse
import com.poti.android.data.remote.dto.request.party.CreatePartyRequestDto
import com.poti.android.data.remote.dto.request.party.PartyJoinRequestDto
import com.poti.android.data.remote.dto.response.artist.ArtistSearchListResponseDto
import com.poti.android.data.remote.dto.response.party.CreatePartyResponseDto
import com.poti.android.data.remote.dto.response.party.MyRecruitListDto
import com.poti.android.data.remote.dto.response.party.PartyDetailResponseDto
import com.poti.android.data.remote.dto.response.party.PartyJoinOptionsDto
import com.poti.android.data.remote.dto.response.party.PartyJoinResponseDto
import com.poti.android.data.remote.dto.response.party.ProductPartyListResponseDto
import com.poti.android.data.remote.dto.response.party.ProductSearchResponseDto
import com.poti.android.data.remote.dto.response.party.ShippingOptionResponseDto
import com.poti.android.data.remote.service.PartyService
import jakarta.inject.Inject

class PartyRemoteDataSource @Inject constructor(
    private val partyService: PartyService,
) {
    suspend fun searchProductTitle(
        artistId: Long,
        keyword: String,
    ): BaseResponse<ProductSearchResponseDto> =
        partyService.searchProductTitle(artistId, keyword)

    suspend fun searchArtist(keyword: String): BaseResponse<ArtistSearchListResponseDto> =
        partyService.searchArtist(keyword)

    suspend fun createParty(body: CreatePartyRequestDto): BaseResponse<CreatePartyResponseDto> =
        partyService.createParty(body)

    suspend fun getShippingOptions(): BaseResponse<List<ShippingOptionResponseDto>> =
        partyService.getShippingOptions()

    suspend fun getPartyDetail(partyId: Long): BaseResponse<PartyDetailResponseDto> =
        partyService.getPartyDetail(partyId = partyId)

    suspend fun getPartyJoinOptions(partyId: Long): BaseResponse<PartyJoinOptionsDto> =
        partyService.getPartyJoinOptions(partyId = partyId)

    suspend fun postPartyJoin(partyJoinRequest: PartyJoinRequestDto): BaseResponse<PartyJoinResponseDto> =
        partyService.postPartyJoin(partyJoinRequest = partyJoinRequest)

    suspend fun getMyRecruitList(status: String): BaseResponse<MyRecruitListDto> =
        partyService.getMyRecruitList(status)

    suspend fun getProductPartyList(
        page: Int?,
        size: Int?,
        title: String,
        artistId: Long,
        sort: String,
        memberIds: List<Long>?,
    ): BaseResponse<ProductPartyListResponseDto> =
        partyService.getProductPartyList(
            page = page,
            size = size,
            title = title,
            artistId = artistId,
            sort = sort,
            memberIds = memberIds,
        )
}

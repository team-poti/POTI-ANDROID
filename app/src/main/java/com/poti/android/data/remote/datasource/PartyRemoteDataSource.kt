package com.poti.android.data.remote.datasource

import com.poti.android.core.network.model.BaseResponse
import com.poti.android.data.remote.dto.request.party.CreatePartyRequestDto
import com.poti.android.data.remote.dto.response.artist.ArtistSearchListResponseDto
import com.poti.android.data.remote.dto.response.history.GroupBuyPostSaleDto
import com.poti.android.data.remote.dto.response.party.CreatePartyResponseDto
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

    suspend fun getPostSale(postId: Long): BaseResponse<GroupBuyPostSaleDto> =
        partyService.getPostSale(postId)
}

package com.poti.android.domain.repository

import com.poti.android.domain.model.artist.ArtistSearchResult
import com.poti.android.domain.model.artist.MemberPriceOption
import com.poti.android.domain.model.delivery.DeliveryOption

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
}

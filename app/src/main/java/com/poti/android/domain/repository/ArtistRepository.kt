package com.poti.android.domain.repository

import com.poti.android.domain.model.artist.Artist
import com.poti.android.domain.model.artist.Member
import com.poti.android.domain.model.artist.MemberPriceOption

interface ArtistRepository {
    suspend fun getArtists(): Result<List<Artist>>

    suspend fun getMemberList(
        artistId: Long,
    ): Result<List<Member>>

    suspend fun getMemberListWithPrice(
        artistId: Long,
    ): Result<List<MemberPriceOption>>
}

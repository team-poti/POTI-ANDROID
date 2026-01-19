package com.poti.android.domain.usecase.post

import com.poti.android.domain.model.artist.ArtistSearchResult
import com.poti.android.domain.repository.PostRepository
import javax.inject.Inject

class SearchArtistUseCase @Inject constructor(
    private val postRepository: PostRepository,
) {
    suspend operator fun invoke(
        keyword: String,
    ): Result<List<ArtistSearchResult>> = postRepository.searchArtist(keyword)
}

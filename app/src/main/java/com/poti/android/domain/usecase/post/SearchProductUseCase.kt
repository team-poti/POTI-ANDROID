package com.poti.android.domain.usecase.post

import com.poti.android.domain.repository.PostRepository
import javax.inject.Inject

class SearchProductUseCase @Inject constructor(
    private val postRepository: PostRepository,
) {
    suspend operator fun invoke(
        artistId: Long,
        keyword: String,
    ): Result<List<String>> = postRepository.searchProductTitle(artistId, keyword)
}

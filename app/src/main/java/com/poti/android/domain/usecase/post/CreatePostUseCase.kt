package com.poti.android.domain.usecase.post

import com.poti.android.domain.model.artist.MemberPriceOption
import com.poti.android.domain.model.delivery.DeliveryOption
import com.poti.android.domain.repository.PostRepository
import javax.inject.Inject

class CreatePostUseCase @Inject constructor(
    private val postRepository: PostRepository,
) {
    suspend operator fun invoke(
        artistId: Long,
        product: String,
        description: String,
        deadline: String,
        bank: String,
        accountNumber: String,
        imageUrls: List<String>,
        options: List<MemberPriceOption>,
        shippings: List<DeliveryOption>,
    ): Result<Long> = postRepository.createPost(
        artistId = artistId,
        product = product,
        description = description,
        deadline = deadline,
        bank = bank,
        accountNumber = accountNumber,
        imageUrls = imageUrls,
        options = options,
        shippings = shippings,
    )
}

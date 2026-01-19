package com.poti.android.domain.usecase.post

import com.poti.android.domain.model.delivery.DeliveryOption
import com.poti.android.domain.repository.PostRepository
import javax.inject.Inject

class GetDeliveryOptionsUseCase @Inject constructor(
    private val postRepository: PostRepository,
) {
    suspend operator fun invoke(): Result<List<DeliveryOption>> = postRepository.getShippingOptions()
}

package com.poti.android.domain.usecase.history

import com.poti.android.domain.model.history.DeliveryDetail
import com.poti.android.domain.model.history.MyPartyList
import com.poti.android.domain.model.history.ParticipantDetail
import com.poti.android.domain.model.history.ParticipantManageDetail
import com.poti.android.domain.model.history.ParticipantPaymentConfirm
import com.poti.android.domain.model.history.RecruiterDetail
import com.poti.android.domain.model.payment.PaymentResult
import com.poti.android.domain.repository.DeliveryRepository
import com.poti.android.domain.repository.ParticipationRepository
import com.poti.android.domain.repository.PartyRepository
import com.poti.android.domain.repository.PaymentRepository
import com.poti.android.domain.repository.ReviewRepository
import javax.inject.Inject

class GetMyRecruitListUseCase @Inject constructor(
    private val partyRepository: PartyRepository,
) {
    suspend operator fun invoke(status: String): Result<MyPartyList> =
        partyRepository.getMyRecruitList(status)
}

class GetMyParticipationListUseCase @Inject constructor(
    private val participationRepository: ParticipationRepository,
) {
    suspend operator fun invoke(status: String): Result<MyPartyList> =
        participationRepository.getMyParticipationList(status)
}

class GetRecruitDetailUseCase @Inject constructor(
    private val partyRepository: PartyRepository,
) {
    suspend operator fun invoke(postId: Long): Result<RecruiterDetail> =
        partyRepository.getRecruitDetail(postId)
}

class GetRecruitParticipantsUseCase @Inject constructor(
    private val partyRepository: PartyRepository,
) {
    suspend operator fun invoke(postId: Long): Result<ParticipantManageDetail> =
        partyRepository.getRecruitPostParticipant(postId)
}

class GetParticipantDetailUseCase @Inject constructor(
    private val participationRepository: ParticipationRepository,
) {
    suspend operator fun invoke(participationId: Long): Result<ParticipantDetail> =
        participationRepository.getParticipantDetail(participationId)
}

class ConfirmPaymentUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository,
) {
    suspend operator fun invoke(orderId: Long): Result<ParticipantPaymentConfirm> =
        paymentRepository.patchPaymentConfirm(orderId)
}

class SubmitPaymentUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository,
) {
    suspend operator fun invoke(
        orderId: Long,
        depositorName: String,
        depositAt: String,
    ): Result<PaymentResult> = paymentRepository.postPayment(orderId, depositorName, depositAt)
}

class RegisterDeliveryUseCase @Inject constructor(
    private val deliveryRepository: DeliveryRepository,
) {
    suspend operator fun invoke(
        orderId: Long,
        deliveryMethod: String,
        trackingNumber: String,
    ): Result<DeliveryDetail> = deliveryRepository.patchDelivery(
        orderId,
        deliveryMethod,
        trackingNumber,
    )
}

class ConfirmDeliveryUseCase @Inject constructor(
    private val participationRepository: ParticipationRepository,
) {
    suspend operator fun invoke(participationId: Long): Result<Long> =
        participationRepository.patchDeliveryConfirm(participationId)
}

class SubmitReviewUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository,
) {
    suspend operator fun invoke(
        transactionId: Long,
        star: Int,
    ): Result<Long> = reviewRepository.postReview(transactionId, star)
}

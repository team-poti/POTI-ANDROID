package com.poti.android.presentation.history.participant

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.repository.ParticipationRepository
import com.poti.android.domain.repository.PaymentRepository
import com.poti.android.domain.repository.ReviewRepository
import com.poti.android.domain.repository.UserRepository
import com.poti.android.presentation.history.navigation.HistoryRoute
import com.poti.android.presentation.history.participant.model.ParticipantDetailOverlayState
import com.poti.android.presentation.history.participant.model.ParticipantDetailUiEffect
import com.poti.android.presentation.history.participant.model.ParticipantDetailUiIntent
import com.poti.android.presentation.history.participant.model.ParticipantDetailUiState
import com.poti.android.presentation.history.participant.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ParticipantViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val participantRepository: ParticipationRepository,
    private val paymentRepository: PaymentRepository,
    private val userRepository: UserRepository,
    private val reviewRepository: ReviewRepository,
) : BaseViewModel<ParticipantDetailUiState, ParticipantDetailUiIntent, ParticipantDetailUiEffect>(
        initialState = ParticipantDetailUiState(),
    ) {
    private val participantId: Long = savedStateHandle.toRoute<HistoryRoute.ParticipantDetail>().participantId

    init {
        getParticipantDetail(participantId)
    }

    override fun processIntent(intent: ParticipantDetailUiIntent) {
        when (intent) {
            is ParticipantDetailUiIntent.LoadDetail -> getParticipantDetail(intent.recruitId)
            ParticipantDetailUiIntent.OnBackClick -> sendEffect(ParticipantDetailUiEffect.NavigateBack)
            is ParticipantDetailUiIntent.OnPartyDetailClick -> sendEffect(ParticipantDetailUiEffect.NavigateToPartyDetail(intent.partyId))
            ParticipantDetailUiIntent.OnDepositCompleteClick -> updateState { copy(overlayState = ParticipantDetailOverlayState.DepositBottomSheet) }
            ParticipantDetailUiIntent.CloseOverlay -> updateState { copy(overlayState = ParticipantDetailOverlayState.None) }
            is ParticipantDetailUiIntent.SubmitDeposit -> patchSubmitDeposit(intent.depositor, intent.depositTime)
            ParticipantDetailUiIntent.OnDeliveredClick -> updateState { copy(overlayState = ParticipantDetailOverlayState.DeliveryConfirmModal) }
            ParticipantDetailUiIntent.ConfirmDelivery -> confirmDelivery()
            ParticipantDetailUiIntent.SkipReview -> updateState { copy(overlayState = ParticipantDetailOverlayState.None) }
            is ParticipantDetailUiIntent.SubmitReview -> submitReview(intent.transactionId, intent.rating)
        }
    }

    private fun getParticipantDetail(participantId: Long) = launchScope {
        if (uiState.value.participantDetailState !is ApiState.Success) {
            updateState { copy(participantDetailState = ApiState.Loading) }
        }

        participantRepository.getParticipantDetail(participantId)
            .onSuccess {
                Timber.d("success: getParticipantDetail")

                updateState { copy(participantDetailState = ApiState.Success(it.toUiModel())) }
            }.onFailure { error ->
                Timber.d("fail: getParticipantDetail")

                updateState { copy(participantDetailState = ApiState.Failure(error.message ?: "fail")) }
            }
    }

    private fun patchSubmitDeposit(
        depositor: String,
        depositTime: String,
    ) = launchScope {
        updateState { copy(participantDetailState = ApiState.Loading) }

        paymentRepository.postPayment(
            orderId = participantId,
            depositorName = depositor,
            depositAt = depositTime,
        ).onSuccess {
            Timber.d("success: $depositor, $depositTime")
        }.onFailure { error ->
            Timber.d("fail: ${error.message}")
        }

        getParticipantDetail(participantId)
        updateState { copy(overlayState = ParticipantDetailOverlayState.None) }
    }

    private fun confirmDelivery() = launchScope {
        participantRepository.patchDeliveryConfirm(participantId)
            .onSuccess { leaderUser ->
                Timber.d("success: confirmDelivery")
                getParticipantDetail(participantId)

                userRepository.getUserProfile(leaderUser)
                    .onSuccess { leader ->
                        Timber.d("confirmDelivery Success")
                        updateState {
                            copy(
                                overlayState = ParticipantDetailOverlayState.DeliveryReviewModal(
                                    recruiterName = leader.nickname,
                                    recruiterProfileUrl = leader.profileImageUrl,
                                    partnerRating = leader.ratingAvg.toString(),
                                ),
                            )
                        }
                    }
                    .onFailure {
                        Timber.e("리더 프로필 조회 실패: ${it.message}")
                        updateState { copy(overlayState = ParticipantDetailOverlayState.None) }
                    }
            }.onFailure {
                Timber.d("fail: confirmDelivery")
                updateState { copy(overlayState = ParticipantDetailOverlayState.None) }
            }
    }

    private fun submitReview(
        transactionId: Long,
        star: Int,
    ) = launchScope {
        reviewRepository.postReview(transactionId, star)
            .onSuccess { result ->
                Timber.d("success: $result")
                updateState { copy(overlayState = ParticipantDetailOverlayState.None) }
                getParticipantDetail(participantId)
            }.onFailure { error ->
                Timber.e(error, "submit review failed")
            }
    }
}

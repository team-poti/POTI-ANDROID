package com.poti.android.presentation.history.participant

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.repository.ParticipationRepository
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
    private val participantRepository: ParticipationRepository
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
            is ParticipantDetailUiIntent.OnPartyDetailClick ->
                sendEffect(ParticipantDetailUiEffect.NavigateToPartyDetail(intent.partyId))

            ParticipantDetailUiIntent.OnDepositCompleteClick -> {
                updateState { copy(overlayState = ParticipantDetailOverlayState.DepositBottomSheet) }
            }

            ParticipantDetailUiIntent.CloseOverlay -> {
                updateState { copy(overlayState = ParticipantDetailOverlayState.None) }
            }

            is ParticipantDetailUiIntent.SubmitDeposit -> patchSubmitDeposit(intent.depositor, intent.depositTime)

            ParticipantDetailUiIntent.OnDeliveredClick -> {
                updateState { copy(overlayState = ParticipantDetailOverlayState.DeliveryConfirmModal) }
            }

            ParticipantDetailUiIntent.ConfirmDelivery -> {
                // TODO: 배송 수령 확인 API 호출 및 주최자 정보 획득
                // 성공 시 리뷰 모달로 전환하며 데이터 전달
                updateState {
                    copy(
                        overlayState = ParticipantDetailOverlayState.DeliveryReviewModal(
                            recruiterName = "장원영", // 더미 데이터
                            recruiterProfileUrl = "", // 더미 데이터
                            partnerRating = "4.5", // 더미 데이터
                        ),
                    )
                }
            }

            ParticipantDetailUiIntent.SkipReview -> {
                // TODO: 리뷰 건너뛰기 처리 (필요시 API 호출)
                updateState { copy(overlayState = ParticipantDetailOverlayState.None) }
            }

            is ParticipantDetailUiIntent.SubmitReview -> {
                // TODO: 리뷰 제출 API 호출
                updateState { copy(overlayState = ParticipantDetailOverlayState.None) }
            }
        }
    }

    private fun getParticipantDetail(participantId: Long) = launchScope {
        updateState {
            copy(participantDetailState = ApiState.Loading)
        }

        participantRepository.getParticipantDetail(participantId)
            .onSuccess {
                Timber.d("success: getParticipantDetail")

                updateState {
                    copy(participantDetailState = ApiState.Success(
                        it.toUiModel()
                    ))
                }
            }.onFailure { error ->
                Timber.d("fail: getParticipantDetail")

                updateState {
                    copy(participantDetailState = ApiState.Failure(error.message ?: "fail"))
                }
            }
    }

    private fun patchSubmitDeposit(
        depositor: String,
        depositTime: String,
    ) = launchScope {
        // TODO: 입금 확인 요청 API 호출
        // 성공 시 overlayState = None 및 데이터 갱신
        updateState { copy(overlayState = ParticipantDetailOverlayState.None) }
    }
}

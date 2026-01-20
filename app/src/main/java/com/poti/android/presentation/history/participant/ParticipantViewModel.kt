package com.poti.android.presentation.history.participant

import androidx.lifecycle.SavedStateHandle
import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.type.ParticipantStatusType
import com.poti.android.presentation.history.DummyParticipantManageDetail
import com.poti.android.presentation.history.participant.model.ParticipantDetailUiEffect
import com.poti.android.presentation.history.participant.model.ParticipantDetailUiIntent
import com.poti.android.presentation.history.participant.model.ParticipantDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ParticipantViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : BaseViewModel<ParticipantDetailUiState, ParticipantDetailUiIntent, ParticipantDetailUiEffect>(
        initialState = ParticipantDetailUiState(),
    ) {
    init {
        processIntent(ParticipantDetailUiIntent.LoadDetail(savedStateHandle["recruitId"] ?: -1L))
    }

    override fun processIntent(intent: ParticipantDetailUiIntent) {
        when (intent) {
            is ParticipantDetailUiIntent.LoadDetail -> loadDetail(intent.recruitId)
            ParticipantDetailUiIntent.OnBackClick -> sendEffect(ParticipantDetailUiEffect.NavigateBack)
            ParticipantDetailUiIntent.OnPartyDetailClick -> navigateToPartyDetail()
            is ParticipantDetailUiIntent.SubmitDeposit -> submitDeposit(intent.depositor, intent.depositTime)
            ParticipantDetailUiIntent.ConfirmDelivery -> confirmDelivery()
            is ParticipantDetailUiIntent.SubmitReview -> submitReview(intent.rating)
            ParticipantDetailUiIntent.SkipReview -> skipReview()
        }
    }

    private fun loadDetail(recruitId: Long) {
        launchScope(
            onError = {
                updateState { copy(participantDetailState = ApiState.Failure(it.message ?: "Unknown Error")) }
            },
        ) {
            updateState { copy(participantDetailState = ApiState.Loading) }

            // TODO: [천민재] 추후 실제 API 연동 필요
            val dummyData = DummyParticipantManageDetail.participantDetailWaitDeposit
            updateState { copy(participantDetailState = ApiState.Success(dummyData)) }
        }
    }

    private fun navigateToPartyDetail() {
        val currentState = uiState.value.participantDetailState
        if (currentState is ApiState.Success) {
            sendEffect(ParticipantDetailUiEffect.NavigateToPartyDetail(currentState.data.recruitId))
        }
    }

    private fun submitDeposit(
        depositor: String,
        depositTime: String,
    ) {
        // TODO: [천민재] 입금 확인 API 연동
        // TODO: [천민재] 모집 완료, 입금 확인중으로 상태 전환
    }

    private fun confirmDelivery() {
        updateState {
            if (participantDetailState is ApiState.Success) {
                copy(
                    participantDetailState = ApiState.Success(
                        participantDetailState.data.copy(
                            userState = ParticipantStatusType.DELIVERY_DONE,
                        ),
                    ),
                )
            } else {
                this
            }
        }
    }

    private fun submitReview(rating: Int) {
        // TODO: 후기 전송 API 연동

        updateState {
            if (participantDetailState is ApiState.Success) {
                copy(
                    participantDetailState = ApiState.Success(
                        participantDetailState.data.copy(
                            userState = ParticipantStatusType.DELIVERY_DONE,
                        ),
                    ),
                )
            } else {
                this
            }
        }
    }

    private fun skipReview() {
        updateState {
            if (participantDetailState is ApiState.Success) {
                copy(
                    participantDetailState = ApiState.Success(
                        participantDetailState.data.copy(
                            userState = ParticipantStatusType.DELIVERY_DONE,
                        ),
                    ),
                )
            } else {
                this
            }
        }
    }
}

package com.poti.android.presentation.history.manage

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.model.history.ParticipantManageDetail
import com.poti.android.domain.type.ParticipantStatusType
import com.poti.android.presentation.history.mapper.toUiModel
import com.poti.android.presentation.history.model.manage.ManageModalState.*
import com.poti.android.presentation.history.model.manage.ParticipantManageUiEffect
import com.poti.android.presentation.history.model.manage.ParticipantManageUiIntent
import com.poti.android.presentation.history.model.manage.ParticipantManageUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParticipantManageViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : BaseViewModel<ParticipantManageUiState, ParticipantManageUiIntent, ParticipantManageUiEffect>(
        initialState = ParticipantManageUiState(),
    ) {
    private val recruitId: Long = savedStateHandle["recruitId"] ?: -1L

    init {
        loadParticipantManageDetail()
    }

    override fun processIntent(intent: ParticipantManageUiIntent) {
        when (intent) {
            ParticipantManageUiIntent.OnBackClick -> {
                sendEffect(ParticipantManageUiEffect.NavigateBack)
            }
            is ParticipantManageUiIntent.OnDepositConfirmClick -> {
                updateState { copy(activeModal = DepositConfirm(intent.participantId)) }
            }
            is ParticipantManageUiIntent.OnDeliveryInputClick -> {
                updateState { copy(activeModal = DeliveryInput(intent.participantId)) }
            }
            ParticipantManageUiIntent.DismissModal -> {
                updateState { copy(activeModal = None) }
            }
            is ParticipantManageUiIntent.ConfirmDeposit -> {
                confirmDeposit(intent.participantId)
            }
            is ParticipantManageUiIntent.RegisterDelivery -> {
                registerDelivery(intent.participantId, intent.deliveryMethod, intent.trackingNumber)
            }
        }
    }

    private fun loadParticipantManageDetail() {
        launchScope(
            onError = { throwable ->
                updateState {
                    copy(
                        participantManageDetail = ApiState.Failure(throwable.message ?: "Unknown Error"),
                    )
                }
            },
        ) {
            updateState { copy(participantManageDetail = ApiState.Loading) }

            // TODO: [천민재] 실제 API 연동 필요 (현재 더미 데이터 사용)
            val dummyData = listOf(
                ParticipantManageDetail(
                    participantId = 1,
                    nickname = "포티",
                    profileImage = null,
                    participantState = ParticipantStatusType.DEPOSIT_CHECK,
                    selectedMember = "장원영",
                    memberPrice = 15000,
                    deliveryMethod = "GS반값택배",
                    deliveryPrice = 1800,
                    depositTime = "2024.12.31 15:30",
                    depositorName = "김철수",
                    recipient = null,
                    phoneNumber = null,
                    zipcode = null,
                    address = null,
                    trackingNumber = null,
                ),
                ParticipantManageDetail(
                    participantId = 2,
                    nickname = "이영희",
                    profileImage = null,
                    participantState = ParticipantStatusType.DELIVERY_WAIT,
                    selectedMember = "카리나",
                    memberPrice = 20000,
                    deliveryMethod = "CU끼리택배",
                    deliveryPrice = 1600,
                    depositTime = null,
                    depositorName = null,
                    recipient = "이영희",
                    phoneNumber = "010-1234-5678",
                    zipcode = "12345",
                    address = "서울특별시 강남구 역삼동",
                    trackingNumber = null,
                ),
            ).map { it.toUiModel() }

            updateState {
                copy(participantManageDetail = ApiState.Success(dummyData))
            }
        }
    }

    private fun confirmDeposit(id: Long) {
        viewModelScope.launch {
            updateState { copy(activeModal = None) }

            // TODO: [천민재] API 전달
        }
    }

    private fun registerDelivery(
        id: Long,
        method: String,
        number: String,
    ) {
        viewModelScope.launch {
            updateState { copy(activeModal = None) }

            // TODO: [천민재] API 전달
        }
    }
}

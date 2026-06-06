package com.poti.android.presentation.history.manage

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.usecase.history.ConfirmPaymentUseCase
import com.poti.android.domain.usecase.history.GetRecruitParticipantsUseCase
import com.poti.android.domain.usecase.history.RegisterDeliveryUseCase
import com.poti.android.presentation.history.manage.model.ManageModalState
import com.poti.android.presentation.history.manage.model.ManageModalState.*
import com.poti.android.presentation.history.manage.model.ParticipantManageUiEffect
import com.poti.android.presentation.history.manage.model.ParticipantManageUiIntent
import com.poti.android.presentation.history.manage.model.ParticipantManageUiState
import com.poti.android.presentation.history.manage.model.toUiModel
import com.poti.android.presentation.history.navigation.HistoryRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ParticipantManageViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getRecruitParticipantsUseCase: GetRecruitParticipantsUseCase,
    private val confirmPaymentUseCase: ConfirmPaymentUseCase,
    private val registerDeliveryUseCase: RegisterDeliveryUseCase,
) : BaseViewModel<ParticipantManageUiState, ParticipantManageUiIntent, ParticipantManageUiEffect>(
        initialState = ParticipantManageUiState(),
    ) {
    private val recruitId: Long = savedStateHandle.toRoute<HistoryRoute.ParticipantManage>().recruitId

    init {
        loadParticipantManageDetail()
    }

    override fun processIntent(intent: ParticipantManageUiIntent) {
        when (intent) {
            ParticipantManageUiIntent.OnBackClick -> sendEffect(ParticipantManageUiEffect.NavigateBack)
            is ParticipantManageUiIntent.OnDepositConfirmClick -> updateState {
                copy(activeModal = DepositConfirm(intent.participantId))
            }
            is ParticipantManageUiIntent.OnDeliveryInputClick -> updateState {
                copy(activeModal = DeliveryInput(intent.participantId))
            }
            ParticipantManageUiIntent.OnDepositModalDismiss -> updateState { copy(activeModal = ManageModalState.None) }
            is ParticipantManageUiIntent.OnDepositModalConfirm -> confirmDeposit(intent.participantId)
            is ParticipantManageUiIntent.RegisterDelivery -> registerDelivery(intent.participantId, intent.deliveryMethod, intent.trackingNumber)
            ParticipantManageUiIntent.OnResume -> loadParticipantManageDetail()
        }
    }

    private fun loadParticipantManageDetail() {
        launchScope {
            if (uiState.value.participantManageDetailLoadState !is ApiState.Success) {
                updateState { copy(participantManageDetailLoadState = ApiState.Loading) }
            }

            getRecruitParticipantsUseCase(recruitId)
                .onSuccess {
                    Timber.d("success: loadParticipantManageDetail")
                    updateState { copy(participantManageDetailLoadState = ApiState.Success(it.toUiModel())) }
                }
                .onFailure { error ->
                    Timber.d("fail: loadParticipantManageDetail")
                    updateState {
                        copy(
                            participantManageDetailLoadState = ApiState.Failure(
                                error.message ?: "Fail: loadParticipantManageDetail",
                            ),
                        )
                    }
                }
        }
    }

    private fun confirmDeposit(id: Long) {
        viewModelScope.launch {
            confirmPaymentUseCase(id)
                .onSuccess {
                    Timber.d("success: confirmDeposit($id)\n\t${it.orderId}, ${it.orderStatus}, ${it.confirmedAt}")
                    updateState { copy(activeModal = ManageModalState.None) }
                    loadParticipantManageDetail()
                }.onFailure { error ->
                    Timber.d("fail: confirmDeposit($id) - ${error.message}")
                }

            updateState { copy(activeModal = ManageModalState.None) }
            loadParticipantManageDetail()
        }
    }

    private fun registerDelivery(
        id: Long,
        method: String,
        number: String,
    ) {
        viewModelScope.launch {
            registerDeliveryUseCase(
                orderId = id,
                deliveryMethod = method,
                trackingNumber = number,
            ).onSuccess {
                updateState { copy(activeModal = ManageModalState.None) }
                loadParticipantManageDetail()
                Timber.d("success: ${it.orderId}, ${it.deliveryStatus}, ${it.trackingNumber}")
            }.onFailure { error ->
                Timber.d("fail: ${error.message}")
            }
        }
    }
}

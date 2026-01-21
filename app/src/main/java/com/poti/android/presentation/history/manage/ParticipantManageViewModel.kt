package com.poti.android.presentation.history.manage

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.model.history.DepositInfo
import com.poti.android.domain.model.history.MemberPriceInfo
import com.poti.android.domain.model.history.ShippingInfo
import com.poti.android.domain.repository.GroupBuyRepository
import com.poti.android.domain.type.ParticipantStatusType
import com.poti.android.presentation.history.manage.model.ManageModalState
import com.poti.android.presentation.history.manage.model.ParticipantManageUiEffect
import com.poti.android.presentation.history.manage.model.ParticipantManageUiIntent
import com.poti.android.presentation.history.manage.model.ParticipantManageUiState
import com.poti.android.presentation.history.manage.model.ParticipantUiModel
import com.poti.android.presentation.history.manage.model.RecruiterManageDetailUiModel
import com.poti.android.presentation.history.manage.model.toUiModel
import com.poti.android.presentation.history.navigation.HistoryRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ParticipantManageViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val groupBuyRepository: GroupBuyRepository
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
                copy(activeModal = ManageModalState.DepositConfirm(intent.participantId))
            }
            is ParticipantManageUiIntent.OnDeliveryInputClick -> updateState {
                copy(activeModal = ManageModalState.DeliveryInput(intent.participantId))
            }
            ParticipantManageUiIntent.OnDepositModalDismiss -> updateState { copy(activeModal = ManageModalState.None) }
            is ParticipantManageUiIntent.OnDepositModalConfirm -> confirmDeposit(intent.participantId)
            is ParticipantManageUiIntent.RegisterDelivery -> registerDelivery(intent.participantId, intent.deliveryMethod, intent.trackingNumber)
        }
    }

    private fun loadParticipantManageDetail() {
        launchScope {
            updateState { copy(participantManageDetailLoadState = ApiState.Loading) }

            groupBuyRepository.getGroupBuyPostParticipant(recruitId)
                .onSuccess {
                    Timber.d("success: loadParticipantManageDetail")
                    updateState { copy(participantManageDetailLoadState = ApiState.Success(it.toUiModel())) }
                }
                .onFailure { error ->
                    Timber.d("fail: loadParticipantManageDetail")
                    updateState {
                        copy(participantManageDetailLoadState = ApiState.Failure(
                            error.message ?: "Fail: loadParticipantManageDetail"
                        ))
                    }
                }

        }
    }

    private fun confirmDeposit(id: Long) {
        viewModelScope.launch {
            updateState { copy(activeModal = ManageModalState.None) }

            // TODO: [천민재] API 전달
        }
    }

    private fun registerDelivery(
        id: Long,
        method: String,
        number: String,
    ) {
        viewModelScope.launch {
            updateState { copy(activeModal = ManageModalState.None) }

            // TODO: [천민재] API 전달
        }
    }
}

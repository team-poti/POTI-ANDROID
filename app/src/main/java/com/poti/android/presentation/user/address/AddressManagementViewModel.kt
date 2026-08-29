package com.poti.android.presentation.user.address

import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.model.delivery.DeliveryInfo
import com.poti.android.domain.usecase.user.GetMyAddressUseCase
import com.poti.android.domain.usecase.user.SaveMyAddressUseCase
import com.poti.android.presentation.user.address.model.AddressManagementUiEffect
import com.poti.android.presentation.user.address.model.AddressManagementUiIntent
import com.poti.android.presentation.user.address.model.AddressManagementUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AddressManagementViewModel @Inject constructor(
    private val getMyAddressUseCase: GetMyAddressUseCase,
    private val saveMyAddressUseCase: SaveMyAddressUseCase,
) : BaseViewModel<AddressManagementUiState, AddressManagementUiIntent, AddressManagementUiEffect>(
        initialState = AddressManagementUiState(),
    ) {
    init {
        loadAddress()
    }

    override fun processIntent(intent: AddressManagementUiIntent) {
        when (intent) {
            AddressManagementUiIntent.OnBackClick -> sendEffect(AddressManagementUiEffect.NavigateBack)
            is AddressManagementUiIntent.OnReceiverNameChange -> updateState {
                copy(receiverName = intent.value, isReceiverNameError = false)
            }
            AddressManagementUiIntent.OnAddressSearchClick -> sendEffect(AddressManagementUiEffect.OpenAddressSearch)
            is AddressManagementUiIntent.OnAddressSelected -> updateState {
                copy(
                    postalCode = intent.postalCode,
                    address = intent.address,
                    detailAddress = "",
                    isPostalCodeError = false,
                    isAddressError = false,
                )
            }
            is AddressManagementUiIntent.OnDetailAddressChange -> updateState {
                copy(detailAddress = intent.value)
            }
            is AddressManagementUiIntent.OnContactChange -> updateState {
                copy(contact = intent.value, isContactError = false)
            }
            AddressManagementUiIntent.OnSaveClick -> handleSaveClick()
        }
    }

    private fun loadAddress() = launchScope {
        updateState { copy(loadState = ApiState.Loading) }

        getMyAddressUseCase()
            .onSuccess { saved ->
                updateState {
                    if (saved == null) {
                        copy(loadState = ApiState.Success(Unit))
                    } else {
                        copy(
                            loadState = ApiState.Success(Unit),
                            savedAddress = saved,
                            receiverName = saved.receiverName,
                            postalCode = saved.zipcode,
                            address = saved.address,
                            detailAddress = saved.addressDetail,
                            contact = saved.phoneNumber,
                        )
                    }
                }
            }
            .onFailure { error ->
                Timber.e(error, "내 배송지 조회 실패")
                updateState { copy(loadState = ApiState.Failure(error.message ?: "Failed")) }
            }
    }

    private fun handleSaveClick() {
        val currentState = uiState.value
        if (currentState.saveState is ApiState.Loading) return
        if (!validateInputs(currentState)) return

        updateState { copy(saveState = ApiState.Loading) }

        launchScope(
            onError = { error ->
                Timber.e(error, "내 배송지 저장 실패")
                updateState { copy(saveState = ApiState.Failure(error.message ?: "Failed")) }
            },
        ) {
            val deliveryInfo = DeliveryInfo(
                receiverName = currentState.receiverName,
                zipcode = currentState.postalCode,
                address = currentState.address,
                addressDetail = currentState.detailAddress,
                phoneNumber = currentState.contact,
            )

            saveMyAddressUseCase(deliveryInfo = deliveryInfo)
                .onSuccess {
                    updateState {
                        copy(
                            savedAddress = deliveryInfo,
                            saveState = ApiState.Success(Unit),
                        )
                    }
                }
                .onFailure { error ->
                    Timber.e(error, "내 배송지 저장 실패")
                    updateState { copy(saveState = ApiState.Failure(error.message ?: "Failed")) }
                }
        }
    }

    private fun validateInputs(state: AddressManagementUiState): Boolean {
        val isReceiverNameEmpty = state.receiverName.isBlank()
        val isPostalCodeEmpty = state.postalCode.isBlank()
        val isAddressEmpty = state.address.isBlank()
        val isContactEmpty = state.contact.isBlank()

        if (isReceiverNameEmpty || isPostalCodeEmpty || isAddressEmpty || isContactEmpty) {
            updateState {
                copy(
                    isReceiverNameError = isReceiverNameEmpty,
                    isPostalCodeError = isPostalCodeEmpty,
                    isAddressError = isAddressEmpty,
                    isContactError = isContactEmpty,
                )
            }
            return false
        }
        return true
    }
}

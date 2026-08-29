package com.poti.android.presentation.user.address.model

import com.poti.android.core.base.UiEffect
import com.poti.android.core.base.UiIntent
import com.poti.android.core.base.UiState
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.model.delivery.DeliveryInfo

data class AddressManagementUiState(
    val receiverName: String = "",
    val postalCode: String = "",
    val address: String = "",
    val detailAddress: String = "",
    val contact: String = "",
    val isReceiverNameError: Boolean = false,
    val isPostalCodeError: Boolean = false,
    val isAddressError: Boolean = false,
    val isContactError: Boolean = false,
    val savedAddress: DeliveryInfo? = null,
    val loadState: ApiState<Unit> = ApiState.Loading,
    val saveState: ApiState<Unit> = ApiState.Init,
) : UiState {
    val isModified: Boolean
        get() = savedAddress?.let {
            receiverName != it.receiverName ||
                postalCode != it.zipcode ||
                address != it.address ||
                detailAddress != it.addressDetail ||
                contact != it.phoneNumber
        } ?: true

    val isSaveEnabled: Boolean
        get() = receiverName.isNotBlank() &&
            postalCode.isNotBlank() &&
            address.isNotBlank() &&
            contact.isNotBlank() &&
            isModified &&
            saveState !is ApiState.Loading
}

sealed interface AddressManagementUiIntent : UiIntent {
    data object OnBackClick : AddressManagementUiIntent

    data class OnReceiverNameChange(val value: String) : AddressManagementUiIntent

    data object OnAddressSearchClick : AddressManagementUiIntent

    data class OnAddressSelected(
        val postalCode: String,
        val address: String,
    ) : AddressManagementUiIntent

    data class OnDetailAddressChange(val value: String) : AddressManagementUiIntent

    data class OnContactChange(val value: String) : AddressManagementUiIntent

    data object OnSaveClick : AddressManagementUiIntent
}

sealed interface AddressManagementUiEffect : UiEffect {
    data object NavigateBack : AddressManagementUiEffect

    data object OpenAddressSearch : AddressManagementUiEffect
}

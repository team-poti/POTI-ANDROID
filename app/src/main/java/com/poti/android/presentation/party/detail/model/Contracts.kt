package com.poti.android.presentation.party.detail.model

import com.poti.android.core.base.UiEffect
import com.poti.android.core.base.UiIntent
import com.poti.android.core.base.UiState
import com.poti.android.core.common.extension.getSuccessDataOrNull
import com.poti.android.core.common.extension.toMoneyString
import com.poti.android.core.common.state.ApiState
import com.poti.android.core.designsystem.component.field.FieldMenuItem
import com.poti.android.domain.model.delivery.DeliveryInfo
import com.poti.android.domain.model.party.PartyDetail
import com.poti.android.domain.model.party.PartyJoinOption
import com.poti.android.domain.type.PartyStatusType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

data class PartyDetailUiState(
    val partyDetail: ApiState<PartyDetail> = ApiState.Loading,
    val showJoinBottomSheet: Boolean = false,
    val partyJoinOption: ApiState<PartyJoinOption> = ApiState.Loading,
    val memberMenuItems: ImmutableList<FieldMenuItem> = persistentListOf(),
    val deliveryMenuItems: ImmutableList<FieldMenuItem> = persistentListOf(),
    val selectedMemberIds: Set<String> = emptySet(),
    val selectedDeliveryIds: Set<String> = emptySet(),
    val orderName: String = "",
    val postalCode: String = "",
    val address: String = "",
    val detailAddress: String = "",
    val contact: String = "",
    val isOrderNameError: Boolean = false,
    val isPostalCodeError: Boolean = false,
    val isAddressError: Boolean = false,
    val isContactError: Boolean = false,
    val savedAddress: DeliveryInfo? = null,
    val isRegisterMyAddressToggle: Boolean = false,
    val isParticipantNoticeModalVisible: Boolean = false,
    val isJoinSuccessDialogVisible: Boolean = false,
) : UiState {
    val isDetailJoinEnable: Boolean
        get() = partyDetail.getSuccessDataOrNull()?.status == PartyStatusType.RECRUITING

    val isRequiredAddressFilled: Boolean
        get() = orderName.isNotBlank() &&
            postalCode.isNotBlank() &&
            address.isNotBlank() &&
            contact.isNotBlank()

    val isAddressModified: Boolean
        get() = savedAddress?.let {
            orderName != it.receiverName ||
                postalCode != it.zipcode ||
                address != it.address ||
                detailAddress != it.addressDetail ||
                contact != it.phoneNumber
        } ?: true

    val isRegisterMyAddressEnable: Boolean
        get() = isRequiredAddressFilled && isAddressModified

    val isRegisterMyAddressChecked: Boolean
        get() = isRegisterMyAddressToggle && isAddressModified

    val selectedMembers: ImmutableList<FieldMenuItem>
        get() = memberMenuItems
            .filter { it.id in selectedMemberIds }
            .toImmutableList()

    val selectedDelivery: FieldMenuItem?
        get() = deliveryMenuItems
            .find { it.id in selectedDeliveryIds }

    val hasSelectedOptions: Boolean
        get() = selectedMemberIds.isNotEmpty() || selectedDeliveryIds.isNotEmpty()

    val totalPrice: String
        get() {
            val memberPriceSum = selectedMembers.sumOf { it.price?.replace(",", "")?.toIntOrNull() ?: 0 }
            val deliveryPrice = selectedDelivery?.price?.replace(",", "")?.toIntOrNull() ?: 0
            return (memberPriceSum + deliveryPrice).toMoneyString()
        }

    val isBottomSheetButtonEnable: Boolean
        get() = selectedMemberIds.isNotEmpty() && selectedDeliveryIds.isNotEmpty()
}

sealed interface PartyDetailIntent : UiIntent {
    data object LoadPartyDetail : PartyDetailIntent

    data object OnBackClick : PartyDetailIntent

    data class OnUploaderClick(val userId: Long) : PartyDetailIntent

    data object OnDetailJoinClick : PartyDetailIntent

    data object OnOptionNextClick : PartyDetailIntent

    data object OnDismissBottomSheet : PartyDetailIntent

    data class OnMemberSelect(val item: FieldMenuItem) : PartyDetailIntent

    data class OnMemberRemove(val id: String) : PartyDetailIntent

    data class OnDeliverySelect(val item: FieldMenuItem) : PartyDetailIntent

    data class OnOrderNameChange(val value: String) : PartyDetailIntent

    data class OnAddressSelected(
        val postalCode: String,
        val address: String,
    ) : PartyDetailIntent

    data class OnDetailAddressChange(val value: String) : PartyDetailIntent

    data class OnContactChange(val value: String) : PartyDetailIntent

    data class OnRegisterMyAddressChange(val checked: Boolean) : PartyDetailIntent

    data object OnFinalJoinClick : PartyDetailIntent

    data object OnParticipantNoticeDismiss : PartyDetailIntent

    data object OnParticipantNoticeConfirm : PartyDetailIntent

    data object OnJoinSuccessConfirm : PartyDetailIntent

    data object OnSystemShareClick : PartyDetailIntent

    data object OnKakaoShareClick : PartyDetailIntent

    data object OnXShareClick : PartyDetailIntent
}

sealed interface PartyDetailEffect : UiEffect {
    data object NavigateBack : PartyDetailEffect

    data object NavigateToJoin : PartyDetailEffect

    data class NavigateToProfile(val userId: Long) : PartyDetailEffect

    data class ReloadDetail(val partyId: Long) : PartyDetailEffect

    data class ShareToSystem(val shareText: String) : PartyDetailEffect

    data class ShareToKakao(
        val title: String,
        val description: String,
        val imageUrl: String,
        val deepLink: String,
    ) : PartyDetailEffect

    data class ShareToX(val shareText: String) : PartyDetailEffect
}

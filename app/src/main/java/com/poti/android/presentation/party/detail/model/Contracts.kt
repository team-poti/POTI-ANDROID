package com.poti.android.presentation.party.detail.model

import com.poti.android.core.base.UiEffect
import com.poti.android.core.base.UiIntent
import com.poti.android.core.base.UiState
import com.poti.android.core.common.extension.getSuccessDataOrNull
import com.poti.android.core.common.extension.toMoneyString
import com.poti.android.core.common.state.ApiState
import com.poti.android.core.designsystem.component.field.FieldMenuItem
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
    val isJoinSuccessDialogVisible: Boolean = false,
) : UiState {
    val isDetailJoinEnable: Boolean
        get() = partyDetail.getSuccessDataOrNull()?.status == PartyStatusType.RECRUITING

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

    data class OnPostalCodeChange(val value: String) : PartyDetailIntent

    data class OnAddressChange(val value: String) : PartyDetailIntent

    data class OnContactChange(val value: String) : PartyDetailIntent

    data object OnFinalJoinClick : PartyDetailIntent

    data object OnJoinSuccessConfirm : PartyDetailIntent
}

sealed interface PartyDetailEffect : UiEffect {
    data object NavigateBack : PartyDetailEffect

    data object NavigateToJoin : PartyDetailEffect

    data class NavigateToProfile(val userId: Long) : PartyDetailEffect

    data class ReloadDetail(val partyId: Long) : PartyDetailEffect
}

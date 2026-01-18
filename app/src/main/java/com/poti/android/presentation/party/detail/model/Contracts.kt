@file:Suppress("ktlint:standard:filename")
package com.poti.android.presentation.party.detail.model

import com.poti.android.core.base.UiEffect
import com.poti.android.core.base.UiIntent
import com.poti.android.core.base.UiState
import com.poti.android.core.common.extension.getSuccessDataOrNull
import com.poti.android.core.common.state.ApiState
import com.poti.android.core.designsystem.component.field.FieldMenuItem
import com.poti.android.domain.model.party.PartyDetail
import com.poti.android.domain.model.party.PartyJoinOption
import com.poti.android.domain.type.PartyStatusType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class PartyDetailUiState(
    val partyDetail: ApiState<PartyDetail> = ApiState.Loading,
    val showJoinBottomSheet: Boolean = false,
    val partyJoinOption: ApiState<PartyJoinOption> = ApiState.Loading,
    val memberMenuItems: ImmutableList<FieldMenuItem> = persistentListOf(),
    val deliveryMenuItems: ImmutableList<FieldMenuItem> = persistentListOf(),
    val selectedMemberIds: Set<String> = emptySet(),
    val selectedDeliveryIds: Set<String> = emptySet(),
    val totalPrice: String = "0",
) : UiState {
    val isJoinEnable: Boolean
        get() = partyDetail.getSuccessDataOrNull()?.status == PartyStatusType.RECRUITING
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
}

sealed interface PartyDetailEffect : UiEffect {
    data object NavigateBack : PartyDetailEffect

    data object NavigateToJoin : PartyDetailEffect

    data class NavigateToProfile(val userId: Long) : PartyDetailEffect
}

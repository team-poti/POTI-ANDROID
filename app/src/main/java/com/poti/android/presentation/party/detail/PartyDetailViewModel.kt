package com.poti.android.presentation.party.detail

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.extension.toMoneyString
import com.poti.android.core.common.state.ApiState
import com.poti.android.core.designsystem.component.field.FieldMenuItem
import com.poti.android.domain.model.delivery.DeliveryOption
import com.poti.android.domain.model.party.Members
import com.poti.android.presentation.party.detail.model.PartyDetailEffect
import com.poti.android.presentation.party.detail.model.PartyDetailEffect.*
import com.poti.android.presentation.party.detail.model.PartyDetailIntent
import com.poti.android.presentation.party.detail.model.PartyDetailUiState
import com.poti.android.presentation.party.detail.navigation.PartyDetailRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject

@HiltViewModel
class PartyDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<PartyDetailUiState, PartyDetailIntent, PartyDetailEffect>(
        initialState = PartyDetailUiState(),
    ) {
    private val args = savedStateHandle.toRoute<PartyDetailRoute.Detail>()
    private val partyId = args.partyId

    init {
        processIntent(PartyDetailIntent.LoadPartyDetail)
    }

    override fun processIntent(intent: PartyDetailIntent) {
        when (intent) {
            PartyDetailIntent.LoadPartyDetail -> fetchPartyDetail()
            PartyDetailIntent.OnBackClick -> sendEffect(PartyDetailEffect.NavigateBack)
            is PartyDetailIntent.OnUploaderClick -> sendEffect(NavigateToProfile(intent.userId))
            PartyDetailIntent.OnDetailJoinClick -> {
                updateState { copy(showJoinBottomSheet = true) }
                fetchPartyJoinOption()
            }
            PartyDetailIntent.OnOptionNextClick -> sendEffect(PartyDetailEffect.NavigateToJoin)
        }
    }

    private fun fetchPartyDetail() = launchScope {
        updateState { copy(partyDetail = ApiState.Loading) }
        // TODO: [지현] 나중에 서버 연결
        updateState { copy(partyDetail = ApiState.Success(dummyPartyDetail)) }
    }

    private fun fetchPartyJoinOption() = launchScope {
        updateState { copy(partyJoinOption = ApiState.Loading) }
        // TODO: [지현] 서버 연결
        updateState {
            copy(
                partyJoinOption = ApiState.Success(dummyJoinOption),
                memberMenuItems = dummyJoinOption.memberOptions.map { it.toFieldMenuItem() }.toImmutableList(),
                deliveryMenuItems = dummyJoinOption.deliveryOptions.map { it.toFieldMenuItem() }.toImmutableList(),
            )
        }
    }

    private fun Members.toFieldMenuItem(): FieldMenuItem =
        FieldMenuItem(
            option = this.memberName,
            price = this.memberPrice.toMoneyString(),
            id = this.memberId.toString(),
        )

    private fun DeliveryOption.toFieldMenuItem(): FieldMenuItem =
        FieldMenuItem(
            option = this.name,
            price = this.price.toMoneyString(),
            id = this.deliveryId.toString(),
        )
}

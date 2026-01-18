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
            PartyDetailIntent.OnBackClick -> sendEffect(NavigateBack)
            is PartyDetailIntent.OnUploaderClick -> sendEffect(NavigateToProfile(intent.userId))
            PartyDetailIntent.OnDetailJoinClick -> {
                updateState { copy(showJoinBottomSheet = true) }
                fetchPartyJoinOption()
            }
            PartyDetailIntent.OnOptionNextClick -> sendEffect(NavigateToJoin)
            PartyDetailIntent.OnDismissBottomSheet -> updateState { copy(showJoinBottomSheet = false) }
            is PartyDetailIntent.OnDeliverySelect -> handleDeliverySelect(intent.item.id)
            is PartyDetailIntent.OnMemberRemove -> handleMemberRemove(intent.id)
            is PartyDetailIntent.OnMemberSelect -> handleMemberSelect(intent.item.id)
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

    private fun handleMemberSelect(selectedId: String) {
        val currentIds = uiState.value.selectedMemberIds.toMutableSet()
        if (selectedId in currentIds) {
            currentIds.remove(selectedId)
        } else {
            currentIds.add(selectedId)
        }
        updateState { copy(selectedMemberIds = currentIds) }
        calculateTotalPrice()
    }

    private fun handleMemberRemove(selectedId: String) {
        val currentIds = uiState.value.selectedMemberIds.toMutableSet()
        currentIds.remove(selectedId)
        updateState { copy(selectedMemberIds = currentIds) }
        calculateTotalPrice()
    }

    private fun handleDeliverySelect(selectedId: String) {
        val newSet = setOf(selectedId)
        updateState { copy(selectedDeliveryIds = newSet) }
        calculateTotalPrice()
    }

    private fun calculateTotalPrice() {
        val currentState = uiState.value

        val memberPriceSum = currentState.memberMenuItems
            .filter { it.id in currentState.selectedMemberIds }
            .sumOf { it.price?.replace(",", "")?.toIntOrNull() ?: 0 }

        val deliveryPriceSum = currentState.deliveryMenuItems
            .filter { it.id in currentState.selectedDeliveryIds }
            .sumOf { it.price?.replace(",", "")?.toIntOrNull() ?: 0 }

        val total = memberPriceSum + deliveryPriceSum
        updateState { copy(totalPrice = total.toMoneyString()) } // 다시 문자열 포맷팅
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

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
import com.poti.android.presentation.party.detail.navigation.PartyDetailGraph
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject

@HiltViewModel
class PartyDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<PartyDetailUiState, PartyDetailIntent, PartyDetailEffect>(
        initialState = PartyDetailUiState(),
    ) {
    private val args = savedStateHandle.toRoute<PartyDetailGraph>()
    private val partyId = args.partyId

    init {
        processIntent(PartyDetailIntent.LoadPartyDetail)
    }

    override fun processIntent(intent: PartyDetailIntent) {
        when (intent) {
            PartyDetailIntent.LoadPartyDetail -> fetchPartyDetail()
            PartyDetailIntent.OnBackClick -> sendEffect(NavigateBack)
            is PartyDetailIntent.OnUploaderClick -> sendEffect(NavigateToProfile(intent.userId))
            PartyDetailIntent.OnDetailJoinClick -> handleDetailJoin()
            PartyDetailIntent.OnOptionNextClick -> sendEffect(NavigateToJoin)
            PartyDetailIntent.OnDismissBottomSheet -> updateState { copy(showJoinBottomSheet = false) }
            is PartyDetailIntent.OnDeliverySelect -> handleDeliverySelect(intent.item.id)
            is PartyDetailIntent.OnMemberRemove -> handleMemberRemove(intent.id)
            is PartyDetailIntent.OnMemberSelect -> handleMemberSelect(intent.item.id)
            is PartyDetailIntent.OnOrderNameChange -> updateState { copy(orderName = intent.value, isOrderNameError = false) }
            is PartyDetailIntent.OnPostalCodeChange -> updateState { copy(postalCode = intent.value, isPostalCodeError = false) }
            is PartyDetailIntent.OnAddressChange -> updateState { copy(address = intent.value, isAddressError = false) }
            is PartyDetailIntent.OnContactChange -> updateState { copy(contact = intent.value, isContactError = false) }
            PartyDetailIntent.OnFinalJoinClick -> {
                if (validateInputs()) {
                    postOrder()
                }
            }
        }
    }

    private fun fetchPartyDetail() = launchScope {
        updateState { copy(partyDetail = ApiState.Loading) }
        // TODO: [지현] 나중에 서버 연결
        updateState { copy(partyDetail = ApiState.Success(dummyPartyDetail)) }
    }

    private fun handleDetailJoin() {
        updateState { copy(showJoinBottomSheet = true) }
        fetchPartyJoinOption()
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
    }

    private fun handleMemberRemove(selectedId: String) {
        val currentIds = uiState.value.selectedMemberIds.toMutableSet()
        currentIds.remove(selectedId)
        updateState { copy(selectedMemberIds = currentIds) }
    }

    private fun handleDeliverySelect(selectedId: String) {
        val newSet = setOf(selectedId)
        updateState { copy(selectedDeliveryId = newSet) }
    }

    private fun validateInputs(): Boolean {
        val currentState = uiState.value
        val isNameEmpty = currentState.orderName.isBlank()
        val isPostalEmpty = currentState.postalCode.isBlank()
        val isAddressEmpty = currentState.address.isBlank()
        val isContactEmpty = currentState.contact.isBlank()

        if (isNameEmpty || isPostalEmpty || isAddressEmpty || isContactEmpty) {
            updateState {
                copy(
                    isOrderNameError = isNameEmpty,
                    isPostalCodeError = isPostalEmpty,
                    isAddressError = isAddressEmpty,
                    isContactError = isContactEmpty,
                )
            }
            return false
        }
        return true
    }

    private fun postOrder() = launchScope {
        // TODO: [지현] 서버 연결
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

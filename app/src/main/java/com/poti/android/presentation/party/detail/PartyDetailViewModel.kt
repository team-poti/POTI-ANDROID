package com.poti.android.presentation.party.detail

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.extension.getSuccessDataOrNull
import com.poti.android.core.common.extension.toMoneyString
import com.poti.android.core.common.state.ApiState
import com.poti.android.core.designsystem.component.field.FieldMenuItem
import com.poti.android.domain.model.delivery.DeliveryOption
import com.poti.android.domain.model.party.DeliveryInfo
import com.poti.android.domain.model.party.JoinOption
import com.poti.android.domain.model.party.Members
import com.poti.android.domain.model.party.PartyJoinInfo
import com.poti.android.domain.usecase.party.GetPartyDetailUseCase
import com.poti.android.domain.usecase.party.GetPartyJoinOptionsUseCase
import com.poti.android.domain.usecase.party.JoinPartyUseCase
import com.poti.android.presentation.party.detail.model.PartyDetailEffect
import com.poti.android.presentation.party.detail.model.PartyDetailEffect.*
import com.poti.android.presentation.party.detail.model.PartyDetailIntent
import com.poti.android.presentation.party.detail.model.PartyDetailUiState
import com.poti.android.presentation.party.detail.navigation.PartyDetailGraph
import com.poti.android.presentation.party.detail.navigation.partyDetailDeepLink
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PartyDetailViewModel @Inject constructor(
    private val getPartyDetailUseCase: GetPartyDetailUseCase,
    private val getPartyJoinOptionsUseCase: GetPartyJoinOptionsUseCase,
    private val joinPartyUseCase: JoinPartyUseCase,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<PartyDetailUiState, PartyDetailIntent, PartyDetailEffect>(
        initialState = PartyDetailUiState(),
    ) {
    private val partyId = savedStateHandle.toRoute<PartyDetailGraph>().partyId

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
            is PartyDetailIntent.OnAddressSelected -> updateState {
                copy(
                    postalCode = intent.postalCode,
                    address = intent.address,
                    detailAddress = "",
                    isPostalCodeError = false,
                    isAddressError = false,
                )
            }

            is PartyDetailIntent.OnDetailAddressChange -> updateState { copy(detailAddress = intent.value) }
            is PartyDetailIntent.OnContactChange -> updateState { copy(contact = intent.value, isContactError = false) }
            PartyDetailIntent.OnFinalJoinClick -> {
                if (validateInputs()) {
                    updateState { copy(isParticipantNoticeModalVisible = true) }
                }
            }

            PartyDetailIntent.OnParticipantNoticeDismiss -> {
                updateState { copy(isParticipantNoticeModalVisible = false) }
            }

            PartyDetailIntent.OnParticipantNoticeConfirm -> {
                updateState { copy(isParticipantNoticeModalVisible = false) }
                postOrder()
            }

            PartyDetailIntent.OnJoinSuccessConfirm -> {
                updateState { copy(isJoinSuccessDialogVisible = false) }
                sendEffect(ReloadDetail(partyId))
            }

            PartyDetailIntent.OnShareClick -> updateState { copy(showShareBottomSheet = true) }
            PartyDetailIntent.OnCopyLinkClick -> {
                closeShareBottomSheet()
                sendEffect(CopyLink(partyDetailDeepLink(partyId)))
            }

            PartyDetailIntent.OnSystemShareClick -> {
                closeShareBottomSheet()
                sendEffect(ShareToSystem(partyDetailDeepLink(partyId)))
            }

            PartyDetailIntent.OnKakaoShareClick -> {
                closeShareBottomSheet()
                handleKakaoShare()
            }

            PartyDetailIntent.OnXShareClick -> {
                closeShareBottomSheet()
                sendEffect(ShareToX(partyDetailDeepLink(partyId)))
            }

            PartyDetailIntent.OnDismissShareBottomSheet -> closeShareBottomSheet()
        }
    }

    private fun handleKakaoShare() {
        val partyDetail = uiState.value.partyDetail.getSuccessDataOrNull() ?: return

        sendEffect(
            ShareToKakao(
                title = partyDetail.title,
                description = "${partyDetail.artist} · ${partyDetail.currentCount}/${partyDetail.totalCount}명 모집 중",
                imageUrl = partyDetail.images.firstOrNull()?.imageUrl.orEmpty(),
                deepLink = partyDetailDeepLink(partyId),
            ),
        )
    }

    private fun fetchPartyDetail() = launchScope {
        updateState { copy(partyDetail = ApiState.Loading) }

        getPartyDetailUseCase(partyId = partyId)
            .onSuccess { partyDetail ->
                Timber.d("getPartyDetail 실행: $partyDetail")
                updateState { copy(partyDetail = ApiState.Success(partyDetail)) }
            }
            .onFailure { error ->
                Timber.d("getPartyDetail 실패: $error")
                updateState { copy(partyDetail = ApiState.Failure(error.message ?: "Failed")) }
            }
    }

    private fun handleDetailJoin() {
        updateState { copy(showJoinBottomSheet = true) }
        fetchPartyJoinOption()
    }

    private fun fetchPartyJoinOption() = launchScope {
        updateState { copy(partyJoinOption = ApiState.Loading) }

        getPartyJoinOptionsUseCase(partyId = partyId)
            .onSuccess { joinOptions ->
                updateState {
                    copy(
                        partyJoinOption = ApiState.Success(joinOptions),
                        memberMenuItems = joinOptions.memberOptions.map { it.toFieldMenuItem() }.toImmutableList(),
                        deliveryMenuItems = joinOptions.deliveryOptions.map { it.toFieldMenuItem() }.toImmutableList(),
                    )
                }
            }
            .onFailure { error ->
                updateState { copy(partyJoinOption = ApiState.Failure(error.message ?: "Failed")) }
            }
    }

    private fun handleMemberSelect(selectedId: String) {
        val currentIds = uiState.value.selectedMemberIds.toMutableSet()
        if (selectedId in currentIds) {
            currentIds.remove(selectedId)
        } else {
            currentIds.add(selectedId)
        }
        updateState { copy(selectedMemberIds = currentIds.toSet()) }
    }

    private fun handleMemberRemove(selectedId: String) {
        val currentIds = uiState.value.selectedMemberIds.toMutableSet()
        currentIds.remove(selectedId)
        updateState { copy(selectedMemberIds = currentIds.toSet()) }
    }

    private fun handleDeliverySelect(selectedId: String) {
        val newSet = setOf(selectedId)
        updateState { copy(selectedDeliveryIds = newSet) }
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
        val currentState = uiState.value

        currentState.selectedDeliveryIds.firstOrNull()?.toLong()?.let { shippingOptionId ->
            val joinItems = currentState.selectedMemberIds.map { idStr ->
                JoinOption(
                    optionId = idStr.toLong(),
                    count = 1,
                )
            }

            val deliveryInfo = DeliveryInfo(
                receiverName = currentState.orderName,
                zipcode = currentState.postalCode,
                address = listOf(currentState.address, currentState.detailAddress)
                    .filter(String::isNotBlank)
                    .joinToString(" "),
                phoneNumber = currentState.contact,
            )

            val joinInfo = PartyJoinInfo(
                partyId = partyId,
                shippingOptionId = shippingOptionId,
                deliveryInfo = deliveryInfo,
                joinItems = joinItems,
            )

            joinPartyUseCase(joinInfo = joinInfo)
                .onSuccess {
                    updateState { copy(isJoinSuccessDialogVisible = true) }
                }
                .onFailure { error ->
                    Timber.e(error, "postPartyJoin 실패")
                }
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

    private fun closeShareBottomSheet() =
        updateState { copy(showShareBottomSheet = false) }
}

package com.poti.android.presentation.party.create

import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.model.artist.Artist
import com.poti.android.domain.model.artist.MemberPriceOption
import com.poti.android.presentation.party.create.model.CreateUiEffect
import com.poti.android.presentation.party.create.model.CreateUiIntent
import com.poti.android.presentation.party.create.model.CreateUiState
import com.poti.android.presentation.party.create.model.FieldError
import com.poti.android.presentation.party.create.model.MemberSettingStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import javax.inject.Inject

@HiltViewModel
class PartyCreateViewModel @Inject constructor() : BaseViewModel<CreateUiState, CreateUiIntent, CreateUiEffect>(
    initialState = CreateUiState(),
) {
    override fun processIntent(intent: CreateUiIntent) {
        when (intent) {
            CreateUiIntent.OnBackClick -> {
                if (uiState.value.isDirty) {
                    sendEffect(CreateUiEffect.ShowDialog)
                } else {
                    sendEffect(CreateUiEffect.NavigateToBack)
                }
            }

            is CreateUiIntent.OnBackConfirm -> {
                updateState { CreateUiState() }
                sendEffect(CreateUiEffect.NavigateToBack)
            }

            is CreateUiIntent.OnImagesChanged -> {
                updateState {
                    copy(
                        isDirty = true,
                        selectedImages = intent.uris.toPersistentList(),
                    )
                }
            }

            is CreateUiIntent.OnArtistSelect -> {
                handleArtistSelect(newAritst = intent.artist)
            }

            is CreateUiIntent.OnAccountNumberChange -> {
                handleAccountNumberChange(newValue = intent.value)
            }

            is CreateUiIntent.OnBankChange -> {
                handleBankChange(newValue = intent.value)
            }

            CreateUiIntent.OnCreateClick -> {
                createParty()
            }

            is CreateUiIntent.OnDeadlineChange -> {
                handleDeadlineChange(newValue = intent.value)
            }

            is CreateUiIntent.OnDeliverySelect -> {
                handleDeliverySelect(newId = intent.deliveryId)
            }

            is CreateUiIntent.OnDescriptionChange -> {
                handleDescriptionChange(newValue = intent.value)
            }

            is CreateUiIntent.OnMemberPriceChange -> {
                handleMemberPriceChange(newOption = intent.option)
            }

            is CreateUiIntent.OnProductChange -> {
                handleProductChange(newValue = intent.value)
            }

            is CreateUiIntent.OnProductSelect -> {
                updateState { copy(productName = intent.product) }
            }

            CreateUiIntent.OnSearchClick -> {
                sendEffect(CreateUiEffect.NavigateToSearch)
            }

            CreateUiIntent.OnAllMemberSelect -> {
                handleAllMemberSelect()
            }

            is CreateUiIntent.OnMemberSelect -> {
                handleMemberSelect(newIndex = intent.index)
            }

            CreateUiIntent.OnMemberSelectDone -> {
                handleMemberSelectDone()
            }

            CreateUiIntent.OnMemberEditClick -> {
                resetDisplayMembers()
                sendEffect(CreateUiEffect.ShowBottomSheet)
            }

            is CreateUiIntent.OnArtistSearchKeywordChange -> {
                handleArtistSearchKeywordChange(intent.value)
            }
        }
    }

    private fun handleAccountNumberChange(newValue: String) {
        updateState {
            copy(
                isDirty = true,
                accountNumber = newValue.filter { it.isDigit() },
                accountNumberError = if (newValue.isNotBlank()) null else this.accountNumberError,
            )
        }
    }

    private fun handleArtistSelect(newAritst: Artist) {
        if (newAritst == uiState.value.selectedArtist) return

        // TODO: [도연] GetMember / settingStatus IN_PROGRESS 변경

        updateState {
            copy(
                isDirty = true,
                selectedArtist = newAritst,
                artistError = null,
                // memberOptions =
            )
        }
    }

    private fun handleArtistSearchKeywordChange(newValue: String) {
        // TODO: [도연] SearchArtist

        updateState {
            copy(
                isDirty = true,
                artistSearchKeyword = newValue,
                // artistSearchResults =
            )
        }
    }

    private fun handleProductChange(newValue: String) {
        // TODO: [도연] SearchProduct

        updateState {
            copy(
                isDirty = true,
                productName = newValue,
                productError = if (newValue.isNotBlank()) null else this.productError,
                // productSearchResults =
            )
        }
    }

    private fun handleBankChange(newValue: String) {
        updateState {
            copy(
                isDirty = true,
                bank = newValue,
                bankError = if (newValue.isNotBlank()) null else this.bankError,
            )
        }
    }

    private fun handleDeadlineChange(newValue: String) {
        updateState {
            copy(
                isDirty = true,
                deadline = newValue,
                deadlineError = if (newValue.isNotBlank()) null else this.deadlineError,
            )
        }
    }

    private fun handleDescriptionChange(newValue: String) {
        updateState {
            copy(
                isDirty = true,
                description = newValue,
                descriptionError = if (newValue.isNotBlank()) null else this.descriptionError,
            )
        }
    }

    private fun handleDeliverySelect(newId: Long) {
        val currentIds = uiState.value.selectedDeliveryIds
        if (currentIds.size < 2 && newId in currentIds) return

        val newIds = if (newId in currentIds) {
            currentIds - newId
        } else {
            currentIds + newId
        }

        updateState {
            copy(
                isDirty = true,
                selectedDeliveryIds = newIds,
            )
        }
    }

    private fun handleMemberPriceChange(newOption: MemberPriceOption) {
        var currentPrice: String? = null

        val newOptions = uiState.value.editableMemberOptions.map { option ->
            if (option == newOption) {
                currentPrice = newOption.price
                newOption
            } else {
                option
            }
        }.toPersistentList()

        val clearError = currentPrice.isNullOrBlank() && newOption.price.isNotBlank()

        updateState {
            copy(
                editableMemberOptions = newOptions,
                memberSettingStatus = if (clearError) MemberSettingStatus.IN_PROGRESS else this.memberSettingStatus,
            )
        }
    }

    private fun handleMemberSelect(newIndex: Int) {
        val currentIndices = uiState.value.sheetDisplayMemberIndices

        val newIndices = if (newIndex in currentIndices) {
            currentIndices - newIndex
        } else {
            currentIndices + newIndex
        }

        updateState {
            copy(
                sheetDisplayMemberIndices = newIndices,
                isSheetTouched = true,
            )
        }
    }

    private fun resetDisplayMembers() {
        val selectedIndices = uiState.value.editableMemberOptions.mapIndexedNotNull { index, option ->
            if (option.memberId in uiState.value.selectedMemberIds) {
                index
            } else {
                null
            }
        }.toSet()

        updateState {
            copy(
                sheetDisplayMemberIndices = selectedIndices,
                isSheetTouched = false,
            )
        }
    }

    private fun handleAllMemberSelect() {
        val newIndices = if (uiState.value.sheetDisplayMemberIndices.isEmpty()) {
            uiState.value.editableMemberOptions.indices.toSet()
        } else {
            setOf()
        }

        updateState {
            copy(
                sheetDisplayMemberIndices = newIndices,
                isSheetTouched = true,
            )
        }
    }

    private fun handleMemberSelectDone() {
        val newIds = uiState.value.editableMemberOptions.mapIndexedNotNull { index, option ->
            if (index in uiState.value.sheetDisplayMemberIndices) {
                option.memberId
            } else {
                null
            }
        }.toSet()

        val newMemberOptions = clearUnselectedOptionPrices(newIds)

        updateState {
            copy(
                selectedMemberIds = newIds,
                editableMemberOptions = newMemberOptions,
                memberSettingStatus = if (newIds.isNotEmpty()) MemberSettingStatus.IN_PROGRESS else this.memberSettingStatus,
            )
        }
    }

    private fun clearUnselectedOptionPrices(newIds: Set<Long>): ImmutableList<MemberPriceOption> {
        return uiState.value.editableMemberOptions.map { option ->
            if (option.memberId in newIds) {
                option
            } else {
                option.copy(price = "")
            }
        }.toPersistentList()
    }

    private fun validateInputs(): Boolean {
        val imageError = if (uiState.value.selectedImages.isEmpty()) FieldError.IMAGE_EMPTY_ERROR else null
        val artistError = if (uiState.value.selectedArtist == null) FieldError.ARTIST_EMPTY_ERROR else null
        val productError = if (uiState.value.productName.isBlank()) FieldError.PRODUCT_EMPTY_ERROR else null
        val deadlineError = if (uiState.value.deadline.isBlank()) FieldError.DEADLINE_EMPTY_ERROR else null
        val descriptionError = if (uiState.value.description.isBlank()) FieldError.DESCRIPTION_ERROR else null
        val accountNumberError = if (uiState.value.accountNumber.isBlank()) FieldError.ACCOUNT_NUMBER_ERROR else null
        val bankError = if (uiState.value.bank.isBlank()) FieldError.BANK_ERROR else null

        val selectedMemberIds = uiState.value.selectedMemberIds
        val currentSettingStatus = when {
            selectedMemberIds.isEmpty() -> MemberSettingStatus.ERROR_NO_MEMBER
            uiState.value.editableMemberOptions.isEmpty() -> MemberSettingStatus.DEFAULT
            uiState.value.editableMemberOptions.any { option -> option.memberId in selectedMemberIds && option.price.isBlank() } -> MemberSettingStatus.ERROR_NO_PRICE
            else -> uiState.value.memberSettingStatus
        }

        val hasMemberOptionError = currentSettingStatus == MemberSettingStatus.ERROR_NO_MEMBER || currentSettingStatus == MemberSettingStatus.ERROR_NO_PRICE

        val hasError = imageError != null || artistError != null || productError != null ||
            deadlineError != null || descriptionError != null ||
            accountNumberError != null || bankError != null || hasMemberOptionError

        if (hasError) {
            updateState {
                copy(
                    imageError = imageError,
                    artistError = artistError,
                    productError = productError,
                    deadlineError = deadlineError,
                    descriptionError = descriptionError,
                    accountNumberError = accountNumberError,
                    bankError = bankError,
                    memberSettingStatus = currentSettingStatus,
                )
            }
        }

        return hasError
    }

    private fun createParty() {
        if (validateInputs() || uiState.value.createPartyState is ApiState.Loading) return

        updateState {
            copy(
                createPartyState = ApiState.Loading,
            )
        }
        // TODO: [도연] 이미지 전처리
        // TODO: [도연] 이미지 업로드
        // TODO: [도연] 포스트 업로드

        updateState {
            copy(
                createPartyState = ApiState.Success(Unit),
            )
        }
    }
}

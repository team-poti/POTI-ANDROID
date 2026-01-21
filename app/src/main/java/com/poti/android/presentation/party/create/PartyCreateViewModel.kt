package com.poti.android.presentation.party.create

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.model.artist.ArtistSearchResult
import com.poti.android.domain.model.artist.MemberPriceOption
import com.poti.android.domain.model.image.ImageInfoForPresigned
import com.poti.android.domain.usecase.artist.GetMembersWithPriceUseCase
import com.poti.android.domain.usecase.image.UploadImagesUseCase
import com.poti.android.domain.usecase.party.CreatePartyUseCase
import com.poti.android.domain.usecase.party.GetDeliveryOptionsUseCase
import com.poti.android.domain.usecase.party.SearchArtistUseCase
import com.poti.android.domain.usecase.party.SearchProductUseCase
import com.poti.android.presentation.party.create.model.CreateUiEffect
import com.poti.android.presentation.party.create.model.CreateUiIntent
import com.poti.android.presentation.party.create.model.CreateUiState
import com.poti.android.presentation.party.create.model.FieldError
import com.poti.android.presentation.party.create.model.MemberSettingStatus
import com.poti.android.presentation.party.util.toImageInfosForPresigned
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

const val IMAGE_TYPE = "POST"

@OptIn(FlowPreview::class)
@HiltViewModel
class PartyCreateViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getMembersWithPriceUseCase: GetMembersWithPriceUseCase,
    private val uploadImagesUseCase: UploadImagesUseCase,
    private val getDeliveryOptionsUseCase: GetDeliveryOptionsUseCase,
    private val searchArtistUseCase: SearchArtistUseCase,
    private val searchProductUseCase: SearchProductUseCase,
    private val createPartyUseCase: CreatePartyUseCase,
) : BaseViewModel<CreateUiState, CreateUiIntent, CreateUiEffect>(
    initialState = CreateUiState(),
) {
    private val _artistSearchKeyword = MutableStateFlow("")
    private val _productSearchKeyword = MutableStateFlow("")

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
                handleArtistSelect(newArtist = intent.artist)
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

    init {
        viewModelScope.launch {
            initializeDeliveryOptions()
        }

        viewModelScope.launch {
            _artistSearchKeyword
                .debounce(500)
                .distinctUntilChanged()
                .filter { it.isNotBlank() }
                .collectLatest { keyword ->
                    searchArtist(keyword)
                }
        }

        viewModelScope.launch {
            _productSearchKeyword
                .debounce(500)
                .distinctUntilChanged()
                .filter { it.isNotBlank() }
                .collectLatest { keyword ->
                    searchProdut(keyword)
                }
        }
    }

    private suspend fun initializeDeliveryOptions() {
        getDeliveryOptionsUseCase()
            .onSuccess { result ->
                updateState {
                    copy(
                        deliveryOptionsState = ApiState.Success(result.toPersistentList()),
                        editableDeliveryOptions = result.toPersistentList(),
                        selectedDeliveryIds = this.selectedDeliveryIds + result.first().deliveryId
                    )
                }
            }.onFailure { e ->
                updateState {
                    copy(
                        deliveryOptionsState = ApiState.Failure(e.message ?: "get delivery fail")
                    )
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

    private fun handleArtistSelect(newArtist: ArtistSearchResult) {
        if (newArtist == uiState.value.selectedArtist) return

        viewModelScope.launch {
            getMembersWithPriceUseCase(newArtist.artistId)
                .onSuccess { result ->
                    updateState {
                        copy(
                            isDirty = true,
                            selectedArtist = newArtist,
                            artistSearchKeyword = newArtist.name,
                            artistError = null,
                            memberOptionsState = ApiState.Success(result.toPersistentList()),
                            editableMemberOptions = result.toPersistentList(),
                            memberSettingStatus = MemberSettingStatus.IN_PROGRESS,
                        )
                    }
                }
                .onFailure {
                    updateState {
                        copy(
                            memberOptionsState = ApiState.Failure(it.message ?: "FAIL")
                        )
                    }
                }
        }
    }

    private fun handleArtistSearchKeywordChange(newValue: String) {
        updateState {
            copy(
                isDirty = true,
                artistSearchKeyword = newValue,
            )
        }

        _artistSearchKeyword.value = newValue
    }

    private suspend fun searchArtist(keyword: String) {
        searchArtistUseCase(keyword = keyword)
            .onSuccess { result ->
                updateState {
                    copy(
                        artistSearchResultsState = ApiState.Success(result.toPersistentList())
                    )
                }
            }
            .onFailure {
                updateState {
                    copy(
                        artistSearchResultsState = ApiState.Failure(it.message ?: "FAIL")
                    )
                }
            }
    }

    private fun handleProductChange(newValue: String) {
        updateState {
            copy(
                isDirty = true,
                productName = newValue,
                productError = if (newValue.isNotBlank()) null else this.productError,
            )
        }
        _productSearchKeyword.value = newValue
    }

    private suspend fun searchProdut(keyword: String) {
        uiState.value.selectedArtist?.let { artist ->
            searchProductUseCase(
                keyword = keyword,
                artistId = artist.artistId,
            )
                .onSuccess { result ->
                    updateState {
                        copy(
                            productSearchResultsState = ApiState.Success(result.toPersistentList())
                        )
                    }
                }
                .onFailure {
                    updateState {
                        copy(
                            productSearchResultsState = ApiState.Failure(it.message ?: "FAIL")
                        )
                    }
                }
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
            if (option.memberId == newOption.memberId) {
                currentPrice = option.price
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

    private fun getConvertedImages(
    ): List<ImageInfoForPresigned> = uiState.value.selectedImages.toImageInfosForPresigned(context)

    private suspend fun getImageUrls(
        imageInfos: List<ImageInfoForPresigned>,
    ): Result<List<String>> = uploadImagesUseCase(IMAGE_TYPE, imageInfos)

    private suspend fun uploadPartyInfo(
        urls: List<String>,
    ): Result<Long> =
        createPartyUseCase(
            artistId = uiState.value.selectedArtist?.artistId ?: 0L,
            product = uiState.value.productName,
            description = uiState.value.description,
            deadline = uiState.value.deadline,
            bank = uiState.value.bank,
            accountNumber = uiState.value.accountNumber,
            imageUrls = urls,
            options = uiState.value.editableMemberOptions.filter { option -> option.memberId in uiState.value.selectedMemberIds },
            shippings = uiState.value.editableDeliveryOptions.filter { option -> option.deliveryId in uiState.value.selectedDeliveryIds }
        )

    private fun createParty() {
        if (validateInputs() || uiState.value.createPartyState is ApiState.Loading) return

        updateState {
            copy(
                createPartyState = ApiState.Loading,
            )
        }

        val imageInfos = getConvertedImages()

        when {
            imageInfos.isEmpty() -> {
                updateState {
                    copy(
                        createPartyState = ApiState.Failure("convert fail"),
                    )
                }
                return
            }

            else -> {
                viewModelScope.launch {
                    getImageUrls(imageInfos)
                        .onSuccess { urls ->
                            uploadPartyInfo(urls)
                        }
                        .onFailure {
                            updateState {
                                copy(createPartyState = ApiState.Failure(it.message ?: "upload fail"))
                            }
                        }
                }
            }
        }
    }
}

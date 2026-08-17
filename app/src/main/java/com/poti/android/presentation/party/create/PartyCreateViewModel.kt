package com.poti.android.presentation.party.create

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.model.artist.ArtistSearchResult
import com.poti.android.domain.model.artist.MemberPriceOption
import com.poti.android.domain.model.delivery.DeliveryOption
import com.poti.android.domain.type.ImageUploadType
import com.poti.android.domain.usecase.artist.GetMembersWithPriceUseCase
import com.poti.android.domain.usecase.image.UploadImagesUseCase
import com.poti.android.domain.usecase.party.CreatePartyUseCase
import com.poti.android.domain.usecase.party.GetDeliveryOptionsUseCase
import com.poti.android.domain.usecase.party.SearchArtistUseCase
import com.poti.android.domain.usecase.party.SearchProductUseCase
import com.poti.android.presentation.party.create.model.CreateModalType
import com.poti.android.presentation.party.create.model.CreateUiEffect
import com.poti.android.presentation.party.create.model.CreateUiEffect.*
import com.poti.android.presentation.party.create.model.CreateUiIntent
import com.poti.android.presentation.party.create.model.CreateUiIntent.*
import com.poti.android.presentation.party.create.model.CreateUiState
import com.poti.android.presentation.party.create.model.DeliveryOptionUiModel
import com.poti.android.presentation.party.create.model.FieldError
import com.poti.android.presentation.party.create.model.MemberSettingStatus
import com.poti.android.presentation.party.create.navigation.PartyCreateGraph
import com.poti.android.presentation.party.create.util.isTodayOrAfter
import com.poti.android.presentation.party.create.util.toDashedDate
import com.poti.android.presentation.party.create.util.toDateOrNull
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.text.filter

@OptIn(FlowPreview::class)
@HiltViewModel
class PartyCreateViewModel @Inject constructor(
    private val getMembersWithPriceUseCase: GetMembersWithPriceUseCase,
    private val uploadImagesUseCase: UploadImagesUseCase,
    private val getDeliveryOptionsUseCase: GetDeliveryOptionsUseCase,
    private val searchArtistUseCase: SearchArtistUseCase,
    private val searchProductUseCase: SearchProductUseCase,
    private val createPartyUseCase: CreatePartyUseCase,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<CreateUiState, CreateUiIntent, CreateUiEffect>(
        initialState = CreateUiState(),
    ) {
    private val params = savedStateHandle.toRoute<PartyCreateGraph>()
    private val paramArtistId = params.artistId
    private val paramArtistName = params.artistName
    private val paramProductName = params.productName

    override fun processIntent(intent: CreateUiIntent) {
        when (intent) {
            OnCloseBottomSheet -> updateState { copy(showMemberBottomSheet = false) }

            OnCloseDialog -> updateState { copy(visibleModal = null) }

            OnBack -> if (shouldShowDialog()) {
                updateState { copy(visibleModal = CreateModalType.EXIT_CONFIRM) }
            } else {
                updateState { copy(visibleModal = null) }
                sendEffect(NavigateToBack)
            }

            OnBackConfirm -> {
                updateState { copy(visibleModal = null) }
                sendEffect(NavigateToBack)
            }

            OnBackToCreate -> {
                sendEffect(NavigateToBack)
            }

            is OnImagesChanged -> updateState {
                copy(
                    imageUris = intent.uris.toPersistentList(),
                    imageError = if (intent.uris.isNotEmpty()) null else this.imageError,
                )
            }

            OnSearchClick -> sendEffect(NavigateToSearch)

            is OnArtistChange -> updateState { copy(artistSearchKeyword = intent.value) }

            is OnArtistSelect -> handleArtistSelect(newArtist = intent.artist)

            is OnProductFocus -> if (uiState.value.isProductFieldReadOnly && intent.focused) {
                updateState { copy(productError = FieldError.ARTIST_EMPTY_ERROR) }
            }

            is OnProductChange -> updateState {
                copy(
                    productName = intent.value,
                    productError = if (intent.value.isNotBlank()) null else this.productError,
                    selectedProduct = "",
                )
            }

            is OnProductSelect -> updateState { copy(productName = intent.product, selectedProduct = intent.product) }

            is OnDeadlineChange -> handleDeadlineChange(newValue = intent.value)

            is OnDescriptionChange -> updateState {
                copy(
                    description = intent.value,
                    descriptionError = if (intent.value.isNotBlank()) null else this.descriptionError,
                )
            }

            is OnAccountNumberChange -> updateState {
                copy(
                    accountNumber = intent.value.filter { it.isDigit() },
                    accountNumberError = if (intent.value.isNotBlank()) null else this.accountNumberError,
                )
            }

            is OnBankChange -> updateState {
                copy(
                    bank = intent.value,
                    bankError = if (intent.value.isNotBlank()) null else this.bankError,
                )
            }

            OnMemberEditClick -> updateState {
                copy(
                    showMemberBottomSheet = true,
                    isMemberBottomSheetTouched = false,
                    tempSelectedMembers = this.selectedMembers,
                )
            }

            is OnMemberSelect -> handleMemberSelect(newMember = intent.member)

            OnAllMemberSelect -> handleAllMemberSelect()

            OnMemberSelectDone -> handleMemberSelectDone()

            is OnMemberPriceChange -> handleMemberPriceChange(newMember = intent.member)

            is OnDeliverySelect -> handleDeliverySelect(newDelivery = intent.delivery)

            is OnDeliveryPriceChange -> handleDeliveryPriceChange(newDelivery = intent.delivery)

            OnCreateClick -> {
                if (uiState.value.createPartyState is ApiState.Loading) return
                if (validateInputs()) return

                updateState { copy(visibleModal = CreateModalType.CREATE_COMPLETE) }
            }

            OnCreateConfirm -> {
                if (uiState.value.createPartyState is ApiState.Loading) return

                updateState { copy(visibleModal = null, createPartyState = ApiState.Loading) }
                createParty()
            }

            OnCloseCreateModal -> updateState { copy(visibleModal = null) }

            ScrollComplete -> updateState { copy(errorIndexToScroll = null) }
        }
    }

    init {
        initializeDeliveryOptions()
        autoFillParams(paramArtistId, paramArtistName, paramProductName)

        viewModelScope.launch {
            uiState
                .map { it.artistSearchKeyword }
                .debounce(500)
                .distinctUntilChanged()
                .collectLatest { keyword ->
                    searchArtist(keyword)
                }
        }

        viewModelScope.launch {
            uiState
                .map { it.productName }
                .debounce(500)
                .distinctUntilChanged()
                .collectLatest { keyword ->
                    searchProduct(keyword)
                }
        }
    }

    private fun shouldShowDialog(): Boolean {
        val state = uiState.value

        val hasUserInput = state.imageUris.isNotEmpty() ||
            state.selectedArtist?.artistId != paramArtistId ||
            state.selectedArtist?.name != paramArtistName ||
            state.productName != (paramProductName ?: "") ||
            state.selectedProduct.isNotEmpty() ||
            state.deadline.isNotEmpty() ||
            state.description.isNotEmpty() ||
            state.accountNumber.isNotEmpty() ||
            state.bank.isNotEmpty() ||
            state.selectedMembers != state.rawMembers ||
            state.deliveryOptions != state.rawDeliveries

        return hasUserInput
    }

    private fun autoFillParams(
        artistId: Long?,
        artistName: String?,
        productName: String?,
    ) {
        if (artistId == null || artistName == null || productName == null) {
            return
        }

        val initialArtist = ArtistSearchResult(
            artistId = artistId,
            name = artistName,
        )

        handleArtistSelect(initialArtist)
        updateState { copy(productName = productName, isAutoFilled = true) }
    }

    private fun initializeDeliveryOptions() {
        viewModelScope.launch {
            getDeliveryOptionsUseCase()
                .onSuccess { result ->
                    val deliveryOptions = result.map {
                        DeliveryOptionUiModel(
                            deliveryId = it.deliveryId,
                            name = it.name,
                            priceInput = it.price.toString(),
                            isSelected = true,
                        )
                    }.toPersistentList()

                    updateState {
                        copy(
                            rawDeliveries = deliveryOptions,
                            deliveryOptions = deliveryOptions,
                        )
                    }
                }.onFailure { e ->
                    Timber.e(e, "get delivery fail")
                }
        }
    }

    private fun handleArtistSelect(newArtist: ArtistSearchResult) {
        val needToLoadMembers = newArtist != uiState.value.selectedArtist

        updateState {
            copy(
                selectedArtist = newArtist,
                artistSearchKeyword = newArtist.name,
                artistError = null,
                isAutoFilled = false,
                productError = if (this.productError == FieldError.ARTIST_EMPTY_ERROR) null else this.productError,
            )
        }

        if (needToLoadMembers) {
            viewModelScope.launch { getMembersByArtistId(newArtist.artistId) }
        }
    }

    private suspend fun getMembersByArtistId(artistId: Long) = getMembersWithPriceUseCase(artistId)
        .onSuccess { data ->
            val members = data.toPersistentList()

            updateState {
                copy(
                    membersState = ApiState.Success(members),
                    rawMembers = members,
                    selectedMembers = members,
                    memberSettingStatus = MemberSettingStatus.EDITABLE,
                    memberError = null,
                )
            }
        }
        .onFailure { e ->
            updateState {
                copy(
                    membersState = ApiState.Failure(e.message ?: "get members fail"),
                )
            }
        }

    private suspend fun searchArtist(keyword: String) = searchArtistUseCase(keyword = keyword)
        .onSuccess { result ->
            updateState {
                copy(
                    artistSearchState = ApiState.Success(result.toPersistentList()),
                )
            }
        }
        .onFailure {
            updateState {
                copy(
                    artistSearchState = ApiState.Failure(it.message ?: "artist search fail"),
                )
            }
        }

    private suspend fun searchProduct(keyword: String) {
        uiState.value.selectedArtist?.let { artist ->
            searchProductUseCase(
                keyword = keyword,
                artistId = artist.artistId,
            )
                .onSuccess { result ->
                    updateState {
                        copy(
                            productSearchState = ApiState.Success(result.toPersistentList()),
                        )
                    }
                }
                .onFailure {
                    updateState {
                        copy(
                            productSearchState = ApiState.Failure(it.message ?: "product search fail"),
                        )
                    }
                }
        }
    }

    private fun handleDeadlineChange(newValue: String) {
        if (!newValue.isDigitsOnly() || newValue.length > 8) return

        updateState {
            copy(
                deadline = newValue,
                deadlineError = if (newValue.isNotBlank()) null else this.deadlineError,
            )
        }
    }

    private fun handleAllMemberSelect() {
        val newMembers = if (uiState.value.rawMembers.size == uiState.value.tempSelectedMembers.size) {
            persistentListOf()
        } else {
            uiState.value.rawMembers
        }

        updateState {
            copy(
                tempSelectedMembers = newMembers,
                isMemberBottomSheetTouched = true,
            )
        }
    }

    private fun handleMemberSelect(newMember: MemberPriceOption) {
        val currentMembers = uiState.value.tempSelectedMembers

        val newMembers = if (currentMembers.any { it.memberId == newMember.memberId }) {
            currentMembers.filter { it.memberId != newMember.memberId }
        } else {
            currentMembers + newMember
        }.toPersistentList()

        updateState {
            copy(
                tempSelectedMembers = newMembers,
                isMemberBottomSheetTouched = true,
            )
        }
    }

    private fun handleMemberSelectDone() {
        val selectedMembers = uiState.value.tempSelectedMembers.sortedBy { temp ->
            uiState.value.rawMembers.indexOfFirst { raw ->
                raw.memberId == temp.memberId
            }
        }.toPersistentList()

        updateState {
            copy(
                selectedMembers = selectedMembers,
                memberSettingStatus = if (selectedMembers.isEmpty()) MemberSettingStatus.MEMBER_NOT_SELECTED else MemberSettingStatus.EDITABLE,
                showMemberBottomSheet = false,
            )
        }
    }

    private fun handleMemberPriceChange(newMember: MemberPriceOption) {
        val newMembers = uiState.value.selectedMembers.map { member ->
            if (member.memberId == newMember.memberId) {
                newMember
            } else {
                member
            }
        }.toPersistentList()

        updateState {
            copy(
                selectedMembers = newMembers,
                memberError = if (!hasInvalidPrice(newMembers)) null else this.memberError,
            )
        }
    }

    private fun handleDeliverySelect(newDelivery: DeliveryOptionUiModel) {
        val currentOptions = uiState.value.deliveryOptions
        val selectedCount = currentOptions.count { it.isSelected }
        val target = currentOptions.find { it.deliveryId == newDelivery.deliveryId } ?: return

        if (selectedCount < 2 && target.isSelected) return

        val newOptions = currentOptions.map {
            if (it.deliveryId == newDelivery.deliveryId) it.copy(isSelected = !it.isSelected) else it
        }.toPersistentList()

        updateState {
            copy(
                deliveryOptions = newOptions,
            )
        }
    }

    private fun handleDeliveryPriceChange(newDelivery: DeliveryOptionUiModel) {
        val newOptions = uiState.value.deliveryOptions.map {
            if (it.deliveryId == newDelivery.deliveryId) newDelivery else it
        }.toPersistentList()

        updateState {
            copy(
                deliveryOptions = newOptions,
                deliveryError = if (!hasInvalidDeliveryPrice(newOptions)) null else this.deliveryError,
            )
        }
    }

    private fun hasInvalidPrice(members: ImmutableList<MemberPriceOption>): Boolean =
        members.any { it.price == "0" || it.price.isBlank() }

    private fun hasInvalidDeliveryPrice(deliveries: ImmutableList<DeliveryOptionUiModel>): Boolean =
        deliveries.filter { it.isSelected }.any { it.priceInput.toIntOrNull() == null }

    private fun DeliveryOptionUiModel.toDeliveryOption(): DeliveryOption =
        DeliveryOption(deliveryId = deliveryId, name = name, price = requireNotNull(priceInput.toIntOrNull()))

    private fun validateInputs(): Boolean {
        val imageError = if (uiState.value.imageUris.isEmpty()) FieldError.IMAGE_EMPTY_ERROR else null
        val artistError = if (uiState.value.selectedArtist == null) FieldError.ARTIST_EMPTY_ERROR else null
        val productError = if (uiState.value.productName.isBlank()) FieldError.PRODUCT_EMPTY_ERROR else null
        val deadlineError = when (uiState.value.deadline.isBlank()) {
            true -> FieldError.DEADLINE_EMPTY_ERROR
            false -> {
                val date = uiState.value.deadline.toDateOrNull()
                when {
                    date == null -> FieldError.DEADLINE_INVALID_ERROR
                    !date.isTodayOrAfter() -> FieldError.DEADLINE_PAST_ERROR
                    else -> null
                }
            }
        }
        val descriptionError = if (uiState.value.description.isBlank()) FieldError.DESCRIPTION_ERROR else null
        val accountNumberError = if (uiState.value.accountNumber.isBlank()) FieldError.ACCOUNT_NUMBER_ERROR else null
        val bankError = if (uiState.value.bank.isBlank()) FieldError.BANK_ERROR else null
        val memberError = when {
            uiState.value.selectedMembers.isEmpty() -> FieldError.MEMBER_EMPTY_ERROR
            hasInvalidPrice(uiState.value.selectedMembers) -> FieldError.MEMBER_PRICE_ERROR
            else -> null
        }
        val deliveryError = if (hasInvalidDeliveryPrice(uiState.value.deliveryOptions)) {
            FieldError.DELIVERY_PRICE_ERROR
        } else {
            null
        }

        val hasError = imageError != null || artistError != null || productError != null ||
            deadlineError != null || descriptionError != null ||
            accountNumberError != null || bankError != null || memberError != null ||
            deliveryError != null

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
                    memberError = memberError,
                    deliveryError = deliveryError,
                )
            }
            getScrollIndex()?.let {
                updateState { copy(errorIndexToScroll = it) }
            }
        }

        return hasError
    }

    private fun getScrollIndex(): Int? {
        val firstErrorFieldIndex = when {
            uiState.value.imageError != null -> 0
            uiState.value.artistError != null -> 1
            uiState.value.productError != null -> 2
            uiState.value.deadlineError != null -> 3
            uiState.value.descriptionError != null -> 4
            uiState.value.accountNumberError != null -> 5
            uiState.value.bankError != null -> 6
            uiState.value.memberError != null -> 7
            uiState.value.deliveryError != null -> 8
            else -> null
        }

        return firstErrorFieldIndex
    }

    private suspend fun uploadPartyInfo(urls: List<String>): Result<Long> = createPartyUseCase(
        artistId = uiState.value.selectedArtist?.artistId ?: 0L,
        product = uiState.value.productName,
        description = uiState.value.description,
        deadline = uiState.value.deadline.toDashedDate(),
        bank = uiState.value.bank,
        accountNumber = uiState.value.accountNumber,
        imageUrls = urls,
        options = uiState.value.selectedMembers,
        shippings = uiState.value.deliveryOptions.filter { it.isSelected }.map { it.toDeliveryOption() },
    )

    private fun createParty() = viewModelScope.launch {
        uploadImagesUseCase(ImageUploadType.POST, uiState.value.imageUris.map { it.toString() })
            .onSuccess { fileNames ->
                uploadPartyInfo(fileNames)
                    .onSuccess { partyId ->
                        updateState {
                            copy(createPartyState = ApiState.Success(partyId))
                        }
                        sendEffect(NavigateToDetail(partyId))
                    }
                    .onFailure {
                        updateState {
                            copy(
                                createPartyState = ApiState.Failure(it.message ?: "create party fail"),
                            )
                        }
                    }
            }
            .onFailure {
                updateState {
                    copy(
                        createPartyState = ApiState.Failure(it.message ?: "image upload fail"),
                    )
                }
            }
    }
}

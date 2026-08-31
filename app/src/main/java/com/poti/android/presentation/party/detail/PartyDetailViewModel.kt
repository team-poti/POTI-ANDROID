package com.poti.android.presentation.party.detail

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.poti.android.BuildConfig
import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.extension.getSuccessDataOrNull
import com.poti.android.core.common.extension.toMoneyString
import com.poti.android.core.common.state.ApiState
import com.poti.android.core.designsystem.component.field.FieldMenuItem
import com.poti.android.core.share.PartyShareContent
import com.poti.android.di.ApplicationScope
import com.poti.android.domain.model.artist.Member
import com.poti.android.domain.model.delivery.DeliveryInfo
import com.poti.android.domain.model.delivery.DeliveryOption
import com.poti.android.domain.model.party.JoinOption
import com.poti.android.domain.model.party.Members
import com.poti.android.domain.model.party.PartyDetail
import com.poti.android.domain.model.party.PartyJoinInfo
import com.poti.android.domain.model.party.PartyJoinOption
import com.poti.android.domain.usecase.artist.GetMembersUseCase
import com.poti.android.domain.usecase.party.GetPartyDetailUseCase
import com.poti.android.domain.usecase.party.GetPartyJoinOptionsUseCase
import com.poti.android.domain.usecase.party.JoinPartyUseCase
import com.poti.android.domain.usecase.user.GetMyAddressUseCase
import com.poti.android.domain.usecase.user.SaveMyAddressUseCase
import com.poti.android.presentation.party.detail.model.PartyDetailEffect
import com.poti.android.presentation.party.detail.model.PartyDetailEffect.*
import com.poti.android.presentation.party.detail.model.PartyDetailIntent
import com.poti.android.presentation.party.detail.model.PartyDetailUiState
import com.poti.android.presentation.party.detail.navigation.PartyDetailGraph
import com.poti.android.presentation.party.detail.navigation.partyDetailDeepLink
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PartyDetailViewModel @Inject constructor(
    private val getPartyDetailUseCase: GetPartyDetailUseCase,
    private val getPartyJoinOptionsUseCase: GetPartyJoinOptionsUseCase,
    private val getMembersUseCase: GetMembersUseCase,
    private val joinPartyUseCase: JoinPartyUseCase,
    private val getMyAddressUseCase: GetMyAddressUseCase,
    private val saveMyAddressUseCase: SaveMyAddressUseCase,
    @ApplicationScope private val applicationScope: CoroutineScope,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<PartyDetailUiState, PartyDetailIntent, PartyDetailEffect>(
        initialState = PartyDetailUiState(),
    ) {
    private val partyId = savedStateHandle.toRoute<PartyDetailGraph>().partyId
    private val deepLink: String = partyDetailDeepLink(partyId)

    private var isMyAddressLoaded = false

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
            is PartyDetailIntent.OnRegisterMyAddressChange -> updateState { copy(isRegisterMyAddressToggle = intent.checked) }
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
                sendEffect(CopyLink(deepLink))
            }

            PartyDetailIntent.OnSystemShareClick -> {
                closeShareBottomSheet()
                sendEffect(ShareToSystem(deepLink))
            }

            PartyDetailIntent.OnKakaoShareClick -> {
                closeShareBottomSheet()
                handleKakaoShare()
            }

            PartyDetailIntent.OnXShareClick -> {
                closeShareBottomSheet()
                handleXShare()
            }

            PartyDetailIntent.OnDismissShareBottomSheet -> closeShareBottomSheet()
        }
    }

    private fun String.toArtistDisplayName(): String {
        val name = trim()
        return name.replace(ENGLISH_NAME_REGEX, "").trim().ifBlank { name }
    }

    private fun handleKakaoShare() {
        val partyDetail = uiState.value.partyDetail.getSuccessDataOrNull() ?: return

        sendEffect(
            ShareToKakao(
                PartyShareContent(
                    artist = partyDetail.artist.toArtistDisplayName(),
                    title = partyDetail.title,
                    description = partyDetail.content,
                    imageUrl = partyDetail.images.firstOrNull()?.imageUrl.orEmpty(),
                    participantCount = partyDetail.currentCount,
                    totalCount = partyDetail.totalCount,
                    host = BuildConfig.DEEP_LINK_HOST,
                    partyId = partyId,
                    deepLink = deepLink,
                ),
            ),
        )
    }

    private fun handleXShare() = launchScope {
        val partyDetail = uiState.value.partyDetail.getSuccessDataOrNull() ?: return@launchScope
        val availableMembers = awaitPartyJoinOption()?.memberOptions
        val allMembers = uiState.value.artistMembers.ifEmpty { fetchArtistMembersForShare(partyDetail.artistId) }

        sendEffect(ShareToX(buildXShareText(partyDetail, availableMembers, allMembers)))
    }

    private suspend fun awaitPartyJoinOption(): PartyJoinOption? =
        when (val option = uiState.value.partyJoinOption) {
            is ApiState.Success -> option.data
            ApiState.Loading -> uiState.first { it.partyJoinOption !is ApiState.Loading }
                .partyJoinOption
                .getSuccessDataOrNull()

            else -> loadPartyJoinOption()
        }

    private suspend fun fetchArtistMembersForShare(artistId: Long): List<Member> {
        val members = getMembersUseCase(artistId = artistId).getOrNull().orEmpty()
        updateState { copy(artistMembers = members.toImmutableList()) }
        return members
    }

    private fun buildXShareText(
        partyDetail: PartyDetail,
        availableMembers: List<Members>?,
        allMembers: List<Member>,
    ): String {
        val availableNames = availableMembers?.map { it.memberName.trim() }
        val allNames = allMembers.map { it.name.trim() }

        val (available, unavailable) = when {
            availableNames == null -> emptyList<String>() to emptyList()
            allNames.isEmpty() -> availableNames to emptyList()
            else -> {
                val availableNameSet = availableNames.toSet()
                allNames.partition { it in availableNameSet }
            }
        }

        val artistName = partyDetail.artist.toArtistDisplayName()
        val artistHashTag = artistName.filter(Char::isLetterOrDigit)

        return buildString {
            appendLine("$artistName ${partyDetail.title}")
            if (available.isNotEmpty() || unavailable.isNotEmpty()) appendLine()
            if (available.isNotEmpty()) appendLine("⭕️ ${available.joinToString(", ")}")
            if (unavailable.isNotEmpty()) appendLine("❌ ${unavailable.joinToString(", ")}")
            appendLine("\n#포티 #분철 #$artistHashTag @poti_kr")
            append("\n$deepLink")
        }
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
        fetchMyAddress()
    }

    private fun fetchPartyJoinOption() = launchScope { loadPartyJoinOption() }

    private suspend fun loadPartyJoinOption(): PartyJoinOption? {
        updateState { copy(partyJoinOption = ApiState.Loading) }

        return getPartyJoinOptionsUseCase(partyId = partyId)
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
            .getOrNull()
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

    private fun fetchMyAddress() {
        if (isMyAddressLoaded) return
        isMyAddressLoaded = true

        launchScope {
            getMyAddressUseCase()
                .onSuccess { saved ->
                    saved ?: return@onSuccess
                    updateState {
                        copy(
                            savedAddress = saved,
                            orderName = orderName.ifBlank { saved.receiverName },
                            postalCode = postalCode.ifBlank { saved.zipcode },
                            address = address.ifBlank { saved.address },
                            detailAddress = detailAddress.ifBlank { saved.addressDetail },
                            contact = contact.ifBlank { saved.phoneNumber },
                        )
                    }
                }
                .onFailure { error ->
                    isMyAddressLoaded = false
                    Timber.e(error, "내 배송지 조회 실패")
                }
        }
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
                address = currentState.address,
                addressDetail = currentState.detailAddress,
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

                    if (currentState.isRegisterMyAddressChecked) {
                        registerMyAddress(deliveryInfo)
                    }
                }
                .onFailure { error ->
                    Timber.e(error, "postPartyJoin 실패")
                }
        }
    }

    private fun registerMyAddress(deliveryInfo: DeliveryInfo) {
        applicationScope.launch {
            saveMyAddressUseCase(deliveryInfo = deliveryInfo)
                .onFailure { error ->
                    Timber.e(error, "내 배송지 저장 실패")
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

    companion object {
        private val ENGLISH_NAME_REGEX = """\s*\((?:[^()]|\([^()]*\))*\)$""".toRegex()
    }
}

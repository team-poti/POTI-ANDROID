package com.poti.android.presentation.party.create.model

import android.net.Uri
import androidx.annotation.StringRes
import com.poti.android.R
import com.poti.android.core.base.UiEffect
import com.poti.android.core.base.UiIntent
import com.poti.android.core.base.UiState
import com.poti.android.core.common.extension.getSuccessDataOrNull
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.model.artist.ArtistSearchResult
import com.poti.android.domain.model.artist.MemberPriceOption
import com.poti.android.domain.model.delivery.DeliveryOption
import com.poti.android.domain.model.image.ImageInfoForPresigned
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

enum class MemberSettingStatus {
    DEFAULT,
    IN_PROGRESS,
    ERROR_NO_MEMBER,
    ERROR_NO_PRICE,
}

enum class FieldError(
    @get:StringRes val message: Int,
) {
    IMAGE_EMPTY_ERROR(R.string.create_error_need_image),
    ARTIST_EMPTY_ERROR(R.string.create_error_need_artist),
    PRODUCT_EMPTY_ERROR(R.string.create_error_need_product),
    DEADLINE_EMPTY_ERROR(R.string.create_error_need_deadline),
    DEADLINE_INVALID_ERROR(R.string.create_error_invalid_date),
    DEADLINE_PAST_ERROR(R.string.create_error_past_deadline),
    DESCRIPTION_ERROR(R.string.create_error_need_description),
    ACCOUNT_NUMBER_ERROR(R.string.create_error_need_account_number),
    BANK_ERROR(R.string.create_error_need_bank),
}

data class CreateUiState(
    val isDirty: Boolean = false,
    val neverShowHint: Boolean = false,
    val selectedImages: ImmutableList<Uri> = persistentListOf(),
    val selectedArtist: ArtistSearchResult? = null,
    val productName: String = "",
    val productSearchResultsState: ApiState<ImmutableList<String>> = ApiState.Init,
    val deadline: String = "",
    val description: String = "",
    val accountNumber: String = "",
    val bank: String = "",
    val memberSettingStatus: MemberSettingStatus = MemberSettingStatus.DEFAULT,
    val memberOptionsState: ApiState<ImmutableList<MemberPriceOption>> = ApiState.Init,
    val editableMemberOptions: ImmutableList<MemberPriceOption> = persistentListOf(),
    val selectedMemberIds: Set<Long> = setOf(),
    val deliveryOptionsState: ApiState<ImmutableList<DeliveryOption>> = ApiState.Init,
    val sheetDisplayMemberIndices: Set<Int> = setOf(),
    val editableDeliveryOptions: ImmutableList<DeliveryOption> = persistentListOf(),
    val selectedDeliveryIds: Set<Long> = setOf(),
    val imageError: FieldError? = null,
    val artistError: FieldError? = null,
    val productError: FieldError? = null,
    val deadlineError: FieldError? = null,
    val descriptionError: FieldError? = null,
    val accountNumberError: FieldError? = null,
    val bankError: FieldError? = null,
    val artistSearchKeyword: String = "",
    val isSheetTouched: Boolean = false,
    val createPartyState: ApiState<Long> = ApiState.Init,
    val artistSearchResultsState: ApiState<ImmutableList<ArtistSearchResult>> = ApiState.Init,
    val neverShowSearchEmptyScreen: Boolean = false,
    val selectedProductName: String = "",
) : UiState {
    val selectedMembersOption = memberOptionsState.getSuccessDataOrNull()?.filter { option -> option.memberId in selectedMemberIds }
    val sheetDisplayMemberNames = editableMemberOptions.map { option -> option.name }
    val editOptionDisplayMembers = editableMemberOptions.filter { option -> option.memberId in selectedMemberIds }.toPersistentList()
    val isArtistSearchResultsEmpty = !neverShowSearchEmptyScreen && artistSearchKeyword.isNotEmpty() && (artistSearchResultsState.getSuccessDataOrNull()?.isEmpty() ?: true)
    val isArtistSelectDoneBtnEnabled = selectedArtist != null
}

sealed interface CreateUiIntent : UiIntent {
    data class InitializeScreen(val artistId: Long?, val artistName: String?, val productName: String?) : CreateUiIntent

    data object CleanScreen : CreateUiIntent

    data object OnBackClick : CreateUiIntent

    data object OnBackConfirm : CreateUiIntent

    data object OnBackToCreate : CreateUiIntent

    data class OnImagesChanged(val uris: List<Uri>) : CreateUiIntent

    data object OnSearchClick : CreateUiIntent

    data class OnArtistSearchKeywordChange(val value: String) : CreateUiIntent

    data class OnArtistSelect(val artist: ArtistSearchResult) : CreateUiIntent

    data class OnProductChange(val value: String) : CreateUiIntent

    data class OnProductSelect(val product: String) : CreateUiIntent

    data class OnDeadlineChange(val value: String) : CreateUiIntent

    data class OnDescriptionChange(val value: String) : CreateUiIntent

    data class OnAccountNumberChange(val value: String) : CreateUiIntent

    data class OnBankChange(val value: String) : CreateUiIntent

    data object OnMemberEditClick : CreateUiIntent

    data class OnMemberSelect(val index: Int) : CreateUiIntent

    data object OnAllMemberSelect : CreateUiIntent

    data object OnMemberSelectDone : CreateUiIntent

    data class OnMemberPriceChange(val option: MemberPriceOption) : CreateUiIntent

    data class OnDeliverySelect(val deliveryId: Long) : CreateUiIntent

    data object OnCreateClick : CreateUiIntent

    data class OnConvertDone(val result: List<ImageInfoForPresigned>) : CreateUiIntent
}

sealed interface CreateUiEffect : UiEffect {
    data object NavigateToBack : CreateUiEffect

    data object NavigateToSearch : CreateUiEffect

    data object ShowBottomSheet : CreateUiEffect

    data object ShowDialog : CreateUiEffect

    data object ConvertUris : CreateUiEffect

    data class NavigateToDetail(val partyId: Long) : CreateUiEffect
}

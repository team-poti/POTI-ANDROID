package com.poti.android.presentation.party.create.model

import android.net.Uri
import androidx.annotation.StringRes
import com.poti.android.R
import com.poti.android.core.base.UiEffect
import com.poti.android.core.base.UiIntent
import com.poti.android.core.base.UiState
import com.poti.android.core.common.extension.getSuccessDataOrNull
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.model.artist.Artist
import com.poti.android.domain.model.artist.MemberPriceOption
import com.poti.android.domain.model.delivery.DeliveryOption
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
    DEADLINE_PAST_ERROR(R.string.create_error_past_deadline),
    DESCRIPTION_ERROR(R.string.create_error_need_description),
    ACCOUNT_NUMBER_ERROR(R.string.create_error_need_account_number),
    BANK_ERROR(R.string.create_error_need_bank),
}

data class CreateUiState(
    val selectedImages: ImmutableList<Uri> = persistentListOf(),
    val selectedArtist: Artist? = null,
    val productName: String = "",
    val productSearchResults: ImmutableList<String> = persistentListOf(),
    val deadline: String = "",
    val description: String = "",
    val accountNumber: String = "",
    val bank: String = "",
    val memberSettingStatus: MemberSettingStatus = MemberSettingStatus.DEFAULT,
    val memberOptionsState: ApiState<ImmutableList<MemberPriceOption>> = ApiState.Init,
    val editableMemberOptions: ImmutableList<MemberPriceOption> = persistentListOf(),
    val selectedMemberIds: Set<Long> = setOf(),
    val sheetDisplayMemberIndices: Set<Int> = setOf(),
    val deliveryOptions: ApiState<ImmutableList<DeliveryOption>> = ApiState.Init,
    val selectedDeliveryIds: Set<Long> = setOf(),
    val imageError: FieldError? = null,
    val artistError: FieldError? = null,
    val productError: FieldError? = null,
    val deadlineError: FieldError? = null,
    val descriptionError: FieldError? = null,
    val accountNumberError: FieldError? = null,
    val bankError: FieldError? = null,
    val artistSearchKeyword: String = "",
    val artistSearchResultsState: ApiState<ImmutableList<Artist>> = ApiState.Init,
    val isSheetTouched: Boolean = false,
    val createPartyState: ApiState<Unit> = ApiState.Init,
) : UiState {
    val sheetDisplayMemberNames = editableMemberOptions.map { option -> option.name }
    val editOptionDisplayMembers = editableMemberOptions.filter { option -> option.memberId in selectedMemberIds }.toPersistentList()
    val isArtistSearchResultsEmpty = artistSearchKeyword.isNotEmpty() && (artistSearchResultsState.getSuccessDataOrNull()?.isEmpty() ?: true)
    val isArtistSelectDoneBtnEnabled = selectedArtist != null
}

sealed interface CreateUiIntent : UiIntent {
    data object OnBackClick : CreateUiIntent

    data class OnImagesChanged(val uris: List<Uri>) : CreateUiIntent

    data object OnSearchClick : CreateUiIntent

    data class OnArtistSearchKeywordChange(val value: String) : CreateUiIntent

    data class OnArtistSelect(val artist: Artist) : CreateUiIntent

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
}

sealed interface CreateUiEffect : UiEffect {
    data object NavigateToBack : CreateUiEffect

    data object NavigateToSearch : CreateUiEffect

    data object ShowBottomSheet : CreateUiEffect
}

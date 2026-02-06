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

enum class MemberSettingStatus {
    ARTIST_NOT_SELECTED,
    MEMBER_NOT_SELECTED,
    EDITABLE,
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
    MEMBER_EMPTY_ERROR(R.string.create_error_need_member),
    MEMBER_PRICE_ERROR(R.string.create_error_need_price),
}

data class CreateUiState(
    val imageUris: List<Uri> = emptyList(),
    val imageError: FieldError? = null,
    val selectedArtist: ArtistSearchResult? = null,
    val artistSearchKeyword: String = "",
    val artistSearchState: ApiState<List<ArtistSearchResult>> = ApiState.Init,
    val artistError: FieldError? = null,
    val selectedProduct: String = "",
    val productName: String = "",
    val productSearchState: ApiState<List<String>> = ApiState.Init,
    val productError: FieldError? = null,
    val deadline: String = "",
    val deadlineError: FieldError? = null,
    val description: String = "",
    val descriptionError: FieldError? = null,
    val accountNumber: String = "",
    val accountNumberError: FieldError? = null,
    val bank: String = "",
    val bankError: FieldError? = null,
    val membersState: ApiState<List<MemberPriceOption>> = ApiState.Init,
    val rawMembers: ImmutableList<MemberPriceOption> = persistentListOf(),
    val selectedMembers: ImmutableList<MemberPriceOption> = persistentListOf(),
    val tempSelectedMembers: ImmutableList<MemberPriceOption> = persistentListOf(),
    val memberSettingStatus: MemberSettingStatus = MemberSettingStatus.ARTIST_NOT_SELECTED,
    val showMemberBottomSheet: Boolean = false,
    val isMemberBottomSheetTouched: Boolean = false,
    val memberError: FieldError? = null,
    val deliveriesState: ApiState<List<DeliveryOption>> = ApiState.Init,
    val rawDeliveries: ImmutableList<DeliveryOption> = persistentListOf(),
    val selectedDeliveries: ImmutableList<DeliveryOption> = persistentListOf(),
    val createPartyState: ApiState<Long> = ApiState.Init,
    val isInitialized: Boolean = false,
    val isArtistAutoFilled: Boolean = false,
    val isFieldTouched: Boolean = false,
    val showDialog: Boolean = false,
    val errorIndexToScroll: Int? = null,
) : UiState {
    val isProductFieldReadOnly = selectedArtist == null
    val isArtistSearchResultsEmpty = !isArtistAutoFilled && artistSearchKeyword.isNotEmpty() && (artistSearchState.getSuccessDataOrNull()?.isEmpty() ?: false)
    val isArtistSelectDoneBtnEnabled = selectedArtist != null
}

sealed interface CreateUiIntent : UiIntent {
    data class InitializeScreen(val artistId: Long?, val artistName: String?, val productName: String?) : CreateUiIntent

    data object OnCloseBottomSheet : CreateUiIntent

    data object OnCloseDialog : CreateUiIntent

    data object OnBack : CreateUiIntent

    data object OnBackConfirm : CreateUiIntent

    data object OnBackToCreate : CreateUiIntent

    data class OnImagesChanged(val uris: List<Uri>) : CreateUiIntent

    data object OnSearchClick : CreateUiIntent

    data class OnArtistChange(val value: String) : CreateUiIntent

    data class OnArtistSelect(val artist: ArtistSearchResult) : CreateUiIntent

    data class OnProductFocus(val focused: Boolean) : CreateUiIntent

    data class OnProductChange(val value: String) : CreateUiIntent

    data class OnProductSelect(val product: String) : CreateUiIntent

    data class OnDeadlineChange(val value: String) : CreateUiIntent

    data class OnDescriptionChange(val value: String) : CreateUiIntent

    data class OnAccountNumberChange(val value: String) : CreateUiIntent

    data class OnBankChange(val value: String) : CreateUiIntent

    data object OnMemberEditClick : CreateUiIntent

    data class OnMemberSelect(val member: MemberPriceOption) : CreateUiIntent

    data object OnAllMemberSelect : CreateUiIntent

    data object OnMemberSelectDone : CreateUiIntent

    data class OnMemberPriceChange(val member: MemberPriceOption) : CreateUiIntent

    data class OnDeliverySelect(val delivery: DeliveryOption) : CreateUiIntent

    data object OnCreateClick : CreateUiIntent

    data object ScrollComplete : CreateUiIntent

    data class ConvertDone(val result: List<ImageInfoForPresigned>) : CreateUiIntent
}

sealed interface CreateUiEffect : UiEffect {
    data object NavigateToBack : CreateUiEffect

    data object NavigateToSearch : CreateUiEffect

    data object ConvertUris : CreateUiEffect

    data class NavigateToDetail(val partyId: Long) : CreateUiEffect
}

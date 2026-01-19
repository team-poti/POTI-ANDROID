package com.poti.android.presentation.party.create.model

import android.net.Uri
import android.view.MenuItem
import androidx.annotation.StringRes
import com.poti.android.R
import com.poti.android.core.base.UiEffect
import com.poti.android.core.base.UiIntent
import com.poti.android.core.base.UiState
import com.poti.android.domain.model.create.Artist
import com.poti.android.domain.model.delivery.DeliveryOption

enum class MemberSettingStatus {
    DEFAULT,
    IN_PROGRESS,
    ERROR_NO_MEMBER,
    ERROR_NO_PRICE,
}

enum class FieldError(
    @get:StringRes val message: Int,
) {
    IMAGE_ERROR(R.string.create_error_need_image),
    ARTIST_ERROR(R.string.create_error_need_artist),
    PRODUCT_ERROR(R.string.create_error_need_product),
    DEADLINE_ERROR(R.string.create_error_need_deadline),
    DESCRIPTION_ERROR(R.string.create_error_need_description),
    ACCOUNT_NUMBER_ERROR(R.string.create_error_need_account_number),
    BANK_ERROR(R.string.create_error_need_bank),
}

data class CreateUiState(
    val selectedImages: List<Uri> = emptyList(),
    val selectedArtist: Artist? = null,
    val productName: String = "",
    val productSearchResults: List<String> = emptyList(),
    val deadline: String = "",
    val description: String = "",
    val accountNumber: String = "",
    val bank: String = "",
    val memberSettingStatus: MemberSettingStatus = MemberSettingStatus.DEFAULT,
    val memberOptions: List<MemberPriceOption> = emptyList(),
    val selectedMemberIds: Set<Long> = setOf(),
    val deliveryOptions: List<DeliveryOption> = emptyList(),
    val selectedDeliveryIds: Set<Long> = setOf(),
    val imageError: FieldError? = null,
    val artistError: FieldError? = null,
    val productError: FieldError? = null,
    val deadlineError: FieldError? = null,
    val descriptionError: FieldError? = null,
    val accountNumberError: FieldError? = null,
    val bankError: FieldError? = null,
    val artistSearchKeyword: String = "",
    val artistSearchResults: List<MenuItem> = emptyList(),
) : UiState {
    val selectedMembersOption = memberOptions.filter { option -> option.memberId in selectedMemberIds }
}

sealed interface CreateUiIntent : UiIntent {}

sealed interface CreateUiEffect : UiEffect {}

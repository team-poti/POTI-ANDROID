package com.poti.android.presentation.party.create.model

import android.net.Uri
import android.view.MenuItem
import com.poti.android.core.base.UiEffect
import com.poti.android.core.base.UiIntent
import com.poti.android.core.base.UiState
import com.poti.android.domain.model.create.Artist
import com.poti.android.domain.model.create.MemberOption
import com.poti.android.domain.model.delivery.DeliveryOption

enum class MemberSettingStatus {
    DEFAULT,
    IN_PROGRESS,
    ERROR_NO_MEMBER,
    ERROR_NO_PRICE,
}

enum class FieldError(
    val message: String,
) {
    IMAGE_ERROR("사진을 1장 이상 등록해주세요"),
    ARTIST_ERROR("아티스트를 선택해주세요"),
    PRODUCT_ERROR("상품 종류를 입력해주세요"),
    DEADLINE_ERROR("모집 기한을 선택해주세요"),
    DESCRIPTION_ERROR("설명을 입력해주세요"),
    ACCOUNT_NUMBER_ERROR("계좌번호를 입력해주세요"),
    BANK_ERROR("은행 정보를 입력해주세요"),
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
    val memberOptions: List<MemberOption> = emptyList(),
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

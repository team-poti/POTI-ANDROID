package com.poti.android.presentation.party.create

import android.net.Uri
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.common.extension.noRippleClickable
import com.poti.android.core.common.util.screenWidthDp
import com.poti.android.core.designsystem.component.display.PotiDivider
import com.poti.android.core.designsystem.component.display.PotiDividerStyle
import com.poti.android.core.designsystem.component.display.PotiErrorMessage
import com.poti.android.core.designsystem.component.field.PotiLongTextField
import com.poti.android.core.designsystem.component.field.PotiShortTextField
import com.poti.android.core.designsystem.component.navigation.PotiBottomButton
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.domain.model.delivery.DeliveryOption
import com.poti.android.presentation.party.create.component.CreateDeliverySetting
import com.poti.android.presentation.party.create.component.CreateMemberSetting
import com.poti.android.presentation.party.create.component.CreatePhotoUpload
import com.poti.android.presentation.party.create.component.CreateProductDropdownField
import com.poti.android.presentation.party.create.component.SellerNotice
import com.poti.android.presentation.party.create.model.CreateUiState
import com.poti.android.presentation.party.create.model.FieldError
import com.poti.android.presentation.party.create.model.MemberPriceOption
import com.poti.android.presentation.party.create.model.MemberSettingStatus

@Composable
fun PartyCreateRoute(modifier: Modifier = Modifier) {
    PartyCreateScreen(
        uiState = CreateUiState(),
        onPopBackStack = {},
        onImageChanged = {},
        onSearchArtist = {},
        onProductChanged = {},
        onProductSearchItemClick = {},
        onDeadlineChanged = {},
        onDescriptionChanged = {},
        onAccountNumberChanged = {},
        onBankChanged = {},
        onMemberPriceChanged = {},
        onMemberEditBtnClick = {},
        onDeliveryRadioBtnClick = {},
        onCreateBtnClick = {},
        modifier = modifier,
    )
}

@Composable
private fun PartyCreateScreen(
    uiState: CreateUiState,
    onPopBackStack: () -> Unit,
    onImageChanged: (List<Uri>) -> Unit,
    onSearchArtist: () -> Unit,
    onProductChanged: (String) -> Unit,
    onProductSearchItemClick: (String) -> Unit,
    onDeadlineChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onAccountNumberChanged: (String) -> Unit,
    onBankChanged: (String) -> Unit,
    onMemberPriceChanged: (MemberPriceOption) -> Unit,
    onMemberEditBtnClick: () -> Unit,
    onDeliveryRadioBtnClick: (Long) -> Unit,
    onCreateBtnClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()

    LaunchedEffect(
        uiState.imageError,
        uiState.artistError,
        uiState.productError,
        uiState.deadlineError,
        uiState.descriptionError,
        uiState.accountNumberError,
        uiState.bankError,
        uiState.memberSettingStatus,
    ) {
        val firstErrorFieldIndex = when {
            uiState.imageError != null -> 0
            uiState.artistError != null -> 1
            uiState.productError != null -> 2
            uiState.deadlineError != null -> 3
            uiState.descriptionError != null -> 4
            uiState.accountNumberError != null -> 5
            uiState.bankError != null -> 6
            uiState.memberSettingStatus == MemberSettingStatus.ERROR_NO_PRICE || uiState.memberSettingStatus == MemberSettingStatus.ERROR_NO_MEMBER -> 7
            else -> null
        }

        firstErrorFieldIndex?.let { index ->
            listState.animateScrollToItem(index)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            PotiHeaderPage(
                onNavigationClick = onPopBackStack,
            )
        },
    ) { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier.padding(innerPadding),
        ) {
            item {
                Text(
                    text = stringResource(R.string.create_label_product_info),
                    modifier = Modifier
                        .padding(horizontal = screenWidthDp(16.dp))
                        .padding(top = 24.dp),
                    color = PotiTheme.colors.black,
                    style = PotiTheme.typography.title18sb,
                )

                CreatePhotoUpload(
                    imageUris = uiState.selectedImages,
                    onImageChanged = onImageChanged,
                    modifier = Modifier.padding(vertical = 20.dp),
                )

                uiState.imageError?.let { error ->
                    PotiErrorMessage(
                        message = stringResource(error.message),
                        modifier = Modifier
                            .padding(horizontal = screenWidthDp(16.dp)),
                    )
                }
            }

            item {
                PotiShortTextField(
                    value = uiState.selectedArtist?.name ?: "",
                    onValueChanged = {},
                    placeholder = stringResource(R.string.create_placeholder_artist),
                    modifier = Modifier
                        .noRippleClickable(onClick = onSearchArtist)
                        .padding(horizontal = screenWidthDp(16.dp))
                        .padding(bottom = 28.dp),
                    enabled = false,
                    label = stringResource(R.string.create_label_artist),
                    error = uiState.artistError?.let { stringResource(it.message) } ?: "",
                    trailingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_search),
                            contentDescription = null,
                            tint = PotiTheme.colors.black,
                            modifier = Modifier.size(18.dp),
                        )
                    },
                )
            }

            item {
                CreateProductDropdownField(
                    value = uiState.productName,
                    onValueChanged = onProductChanged,
                    searchResults = uiState.productSearchResults,
                    onItemClick = onProductSearchItemClick,
                    modifier = Modifier
                        .padding(bottom = 28.dp),
                    fieldErrorMsg = uiState.productError?.let { stringResource(it.message) } ?: "",
                )
            }

            item {
                PotiShortTextField(
                    value = uiState.deadline,
                    onValueChanged = onDeadlineChanged,
                    placeholder = stringResource(R.string.create_placeholder_deadline),
                    modifier = Modifier
                        .padding(horizontal = screenWidthDp(16.dp))
                        .padding(bottom = 28.dp),
                    label = stringResource(R.string.create_label_deadline),
                    error = uiState.deadlineError?.let { stringResource(it.message) } ?: "",
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
                )
            }

            item {
                PotiLongTextField(
                    value = uiState.description,
                    onValueChanged = onDescriptionChanged,
                    placeholder = stringResource(R.string.create_placeholder_description),
                    modifier = Modifier
                        .padding(horizontal = screenWidthDp(16.dp))
                        .padding(bottom = 28.dp),
                    label = stringResource(R.string.create_label_description),
                    error = uiState.descriptionError?.let { stringResource(it.message) } ?: "",
                )
            }

            item {
                PotiShortTextField(
                    value = uiState.accountNumber,
                    onValueChanged = onAccountNumberChanged,
                    placeholder = stringResource(R.string.create_placeholder_account_number),
                    modifier = Modifier
                        .padding(horizontal = screenWidthDp(16.dp))
                        .padding(bottom = 28.dp),
                    label = stringResource(R.string.create_label_account_number),
                    error = uiState.accountNumberError?.let { stringResource(it.message) } ?: "",
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                )
            }

            item {
                PotiShortTextField(
                    value = uiState.bank,
                    onValueChanged = onBankChanged,
                    placeholder = stringResource(R.string.create_placeholder_bank),
                    modifier = Modifier
                        .padding(horizontal = screenWidthDp(16.dp))
                        .padding(bottom = 24.dp),
                    label = stringResource(R.string.create_label_bank),
                    error = uiState.bankError?.let { stringResource(it.message) } ?: "",
                )
            }

            item {
                PotiDivider(
                    styleType = PotiDividerStyle.LARGE,
                )

                CreateMemberSetting(
                    status = uiState.memberSettingStatus,
                    selectedMembersOption = uiState.selectedMembersOption,
                    onPriceChange = onMemberPriceChanged,
                    onEditBtnClick = onMemberEditBtnClick,
                )
            }

            item {
                PotiDivider(
                    styleType = PotiDividerStyle.LARGE,
                )

                CreateDeliverySetting(
                    deliveryOptions = uiState.deliveryOptions,
                    selectedOptionIds = uiState.selectedDeliveryIds,
                    onDeliveryOptionClick = onDeliveryRadioBtnClick,
                )
            }

            item {
                PotiDivider(
                    styleType = PotiDividerStyle.LARGE,
                )

                SellerNotice(
                    modifier = Modifier
                        .padding(bottom = 40.dp),
                )

                PotiBottomButton(
                    text = stringResource(R.string.create_btn_create),
                    onClick = onCreateBtnClick,
                )
            }
        }
    }
}

@Preview
@Composable
private fun PartyCreateScreenDefaultPreview() {
    val deliveryOptions = listOf(
        DeliveryOption(deliveryId = 1, name = "일반택배", price = 4000),
        DeliveryOption(deliveryId = 2, name = "준등기", price = 1800),
    )
    val selectedDeliveryIds = setOf(1.toLong())

    PotiTheme {
        PartyCreateScreen(
            uiState = CreateUiState(
                deliveryOptions = deliveryOptions,
                selectedDeliveryIds = selectedDeliveryIds,
            ),
            onPopBackStack = {},
            onImageChanged = {},
            onSearchArtist = {},
            onProductChanged = {},
            onProductSearchItemClick = {},
            onDeadlineChanged = {},
            onDescriptionChanged = {},
            onAccountNumberChanged = {},
            onBankChanged = {},
            onMemberPriceChanged = {},
            onMemberEditBtnClick = {},
            onDeliveryRadioBtnClick = {},
            onCreateBtnClick = {},
        )
    }
}

@Preview
@Composable
private fun PartyCreateScreenAccountNumberErrorPreview() {
    val deliveryOptions = listOf(
        DeliveryOption(deliveryId = 1, name = "일반택배", price = 4000),
        DeliveryOption(deliveryId = 2, name = "준등기", price = 1800),
    )
    var accountNumberError by remember { mutableStateOf<FieldError?>(null) }

    PotiTheme {
        PartyCreateScreen(
            uiState = CreateUiState(
                deliveryOptions = deliveryOptions,
                accountNumberError = accountNumberError,
            ),
            onPopBackStack = {},
            onImageChanged = {},
            onSearchArtist = {},
            onProductChanged = {},
            onProductSearchItemClick = {},
            onDeadlineChanged = {},
            onDescriptionChanged = {},
            onAccountNumberChanged = {},
            onBankChanged = {},
            onMemberPriceChanged = {},
            onMemberEditBtnClick = {},
            onDeliveryRadioBtnClick = {},
            onCreateBtnClick = {
                accountNumberError = FieldError.ACCOUNT_NUMBER_ERROR
            },
        )
    }
}

@Preview
@Composable
private fun PartyCreateMemberPreview() {
    val deliveryOptions = listOf(
        DeliveryOption(deliveryId = 1, name = "일반택배", price = 4000),
        DeliveryOption(deliveryId = 2, name = "준등기", price = 1800),
    )

    PotiTheme {
        PartyCreateScreen(
            uiState = CreateUiState(
                deliveryOptions = deliveryOptions,
                memberSettingStatus = MemberSettingStatus.ERROR_NO_MEMBER,
            ),
            onPopBackStack = {},
            onImageChanged = {},
            onSearchArtist = {},
            onProductChanged = {},
            onProductSearchItemClick = {},
            onDeadlineChanged = {},
            onDescriptionChanged = {},
            onAccountNumberChanged = {},
            onBankChanged = {},
            onMemberPriceChanged = {},
            onMemberEditBtnClick = {},
            onDeliveryRadioBtnClick = {},
            onCreateBtnClick = {},
        )
    }
}

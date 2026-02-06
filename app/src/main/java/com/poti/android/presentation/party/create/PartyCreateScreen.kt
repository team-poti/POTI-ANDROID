package com.poti.android.presentation.party.create

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poti.android.R
import com.poti.android.core.common.extension.getSuccessDataOrNull
import com.poti.android.core.common.extension.noRippleClickable
import com.poti.android.core.common.util.HandleSideEffects
import com.poti.android.core.common.util.screenWidthDp
import com.poti.android.core.designsystem.component.display.PotiDivider
import com.poti.android.core.designsystem.component.display.PotiDividerStyle
import com.poti.android.core.designsystem.component.display.PotiErrorMessage
import com.poti.android.core.designsystem.component.field.PotiLongTextField
import com.poti.android.core.designsystem.component.field.PotiShortTextField
import com.poti.android.core.designsystem.component.modal.PotiSmallModal
import com.poti.android.core.designsystem.component.navigation.PotiBottomButton
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.domain.model.artist.MemberPriceOption
import com.poti.android.domain.model.delivery.DeliveryOption
import com.poti.android.presentation.party.component.MemberSelectBottomSheet
import com.poti.android.presentation.party.create.component.CreateDeliverySetting
import com.poti.android.presentation.party.create.component.CreateDropdownField
import com.poti.android.presentation.party.create.component.CreateMemberSetting
import com.poti.android.presentation.party.create.component.CreatePhotoUpload
import com.poti.android.presentation.party.create.component.SellerNotice
import com.poti.android.presentation.party.create.component.ViewType
import com.poti.android.presentation.party.create.model.CreateUiEffect.*
import com.poti.android.presentation.party.create.model.CreateUiIntent.*
import com.poti.android.presentation.party.create.model.CreateUiState
import com.poti.android.presentation.party.create.util.DateTransformation
import com.poti.android.presentation.party.create.util.toImageInfosForPresigned

@Composable
fun PartyCreateRoute(
    onPopBackStack: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    viewModel: PartyCreateViewModel,
    modifier: Modifier = Modifier,
    artistId: Long? = null,
    artistName: String? = null,
    productName: String? = null,
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(artistId, artistName, productName) {
        viewModel.processIntent(InitializeScreen(artistId, artistName, productName))
    }

    BackHandler {
        viewModel.processIntent(OnBack)
    }

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            NavigateToBack -> {
                onPopBackStack()
            }

            NavigateToSearch -> onNavigateToSearch()

            ConvertUris -> {
                val result = uiState.imageUris.toImageInfosForPresigned(context)
                viewModel.processIntent(ConvertDone(result))
            }

            is NavigateToDetail -> {
                onNavigateToDetail(effect.partyId)
            }
        }
    }

    if (uiState.showMemberBottomSheet) {
        MemberSelectBottomSheet(
            title = stringResource(R.string.create_title_bottomsheet),
            mainBtnText = stringResource(R.string.action_button_done),
            subBtnText = stringResource(R.string.action_button_select_all),
            onDismiss = { viewModel.processIntent(OnCloseBottomSheet) },
            onMainBtnClick = { viewModel.processIntent(OnMemberSelectDone) },
            onSubBtnClick = { viewModel.processIntent(OnAllMemberSelect) },
            allMembers = uiState.rawMembers,
            selectedMembers = uiState.tempSelectedMembers,
            onMemberClick = { viewModel.processIntent(OnMemberSelect(it)) },
            memberToName = { it.name },
            memberToId = { it.memberId },
            mainEnabled = uiState.isMemberBottomSheetTouched,
            subEnabled = true,
            autoCloseSubBtn = false,
        )
    }

    if (uiState.showDialog) {
        PotiSmallModal(
            onDismissRequest = { viewModel.processIntent(OnCloseDialog) },
            title = stringResource(R.string.create_exit_dialog_title),
            text = stringResource(R.string.create_exit_dialog_content),
            dismissBtnText = stringResource(R.string.create_exit_dialog_dismiss_text),
            confirmBtnText = stringResource(R.string.create_exit_dialog_confirm_text),
            onDismissBtnClick = { viewModel.processIntent(OnBackConfirm) },
            onConfirmBtnClick = { viewModel.processIntent(OnCloseDialog) },
        )
    }

    PartyCreateScreen(
        uiState = uiState,
        onScrollComplete = { viewModel.processIntent(ScrollComplete) },
        onBackClick = { viewModel.processIntent(OnBack) },
        onImageChanged = { viewModel.processIntent(OnImagesChanged(it)) },
        onSearchArtist = { viewModel.processIntent(OnSearchClick) },
        onProductFocusChanged = { viewModel.processIntent(OnProductFocus(it)) },
        onProductChanged = { viewModel.processIntent(OnProductChange(it)) },
        onProductSearchItemClick = { viewModel.processIntent(OnProductSelect(it)) },
        onDeadlineChanged = { viewModel.processIntent(OnDeadlineChange(it)) },
        onDescriptionChanged = { viewModel.processIntent(OnDescriptionChange(it)) },
        onAccountNumberChanged = { viewModel.processIntent(OnAccountNumberChange(it)) },
        onBankChanged = { viewModel.processIntent(OnBankChange(it)) },
        onMemberPriceChanged = { viewModel.processIntent(OnMemberPriceChange(it)) },
        onMemberEditBtnClick = { viewModel.processIntent(OnMemberEditClick) },
        onDeliveryRadioBtnClick = { viewModel.processIntent(OnDeliverySelect(it)) },
        onCreateBtnClick = { viewModel.processIntent(OnCreateClick) },
        modifier = modifier,
    )
}

@Composable
private fun PartyCreateScreen(
    uiState: CreateUiState,
    onScrollComplete: () -> Unit,
    onBackClick: () -> Unit,
    onImageChanged: (List<Uri>) -> Unit,
    onSearchArtist: () -> Unit,
    onProductFocusChanged: (Boolean) -> Unit,
    onProductChanged: (String) -> Unit,
    onProductSearchItemClick: (String) -> Unit,
    onDeadlineChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onAccountNumberChanged: (String) -> Unit,
    onBankChanged: (String) -> Unit,
    onMemberPriceChanged: (MemberPriceOption) -> Unit,
    onMemberEditBtnClick: () -> Unit,
    onDeliveryRadioBtnClick: (DeliveryOption) -> Unit,
    onCreateBtnClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val dateTransformation = remember { DateTransformation() }

    LaunchedEffect(uiState.errorIndexToScroll) {
        uiState.errorIndexToScroll?.let {
            listState.animateScrollToItem(it)
        }
        onScrollComplete()
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            PotiHeaderPage(
                onNavigationClick = onBackClick,
            )
        },
        bottomBar = {
            PotiBottomButton(
                text = stringResource(R.string.create_btn_create),
                onClick = onCreateBtnClick,
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
                        .padding(top = 24.dp, bottom = 20.dp),
                    color = PotiTheme.colors.black,
                    style = PotiTheme.typography.title18sb,
                )

                CreatePhotoUpload(
                    imageUris = uiState.imageUris,
                    onImageChanged = onImageChanged,
                )

                uiState.imageError?.let { error ->
                    PotiErrorMessage(
                        message = stringResource(error.message),
                        modifier = Modifier
                            .padding(horizontal = screenWidthDp(16.dp))
                            .padding(top = 2.dp, bottom = 8.dp),
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
                        .padding(top = 20.dp, bottom = 28.dp),
                    enabled = false,
                    label = stringResource(R.string.create_label_artist),
                    error = uiState.artistError?.let { stringResource(it.message) } ?: "",
                    trailingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_search),
                            contentDescription = null,
                            tint = PotiTheme.colors.gray700,
                            modifier = Modifier.size(24.dp),
                        )
                    },
                )
            }

            item {
                CreateDropdownField(
                    viewType = ViewType.CREATE_PARTY,
                    value = uiState.productName,
                    onValueChanged = onProductChanged,
                    searchResults = uiState.productSearchState.getSuccessDataOrNull() ?: emptyList(),
                    onItemClick = onProductSearchItemClick,
                    placeholder = stringResource(R.string.create_placeholder_product),
                    label = stringResource(R.string.create_label_product),
                    resultToString = { it },
                    modifier = Modifier
                        .padding(bottom = 28.dp),
                    fieldErrorMsg = uiState.productError?.let { stringResource(it.message) } ?: "",
                    selectedString = uiState.selectedProduct,
                    readOnly = uiState.isProductFieldReadOnly,
                    onFocusChanged = onProductFocusChanged,
                )
            }

            item {
                PotiShortTextField(
                    value = uiState.deadline,
                    onValueChanged = onDeadlineChanged,
                    placeholder = stringResource(R.string.create_placeholder_deadline),
                    modifier = Modifier
                        .animateItem(
                            placementSpec = tween(durationMillis = 120, easing = LinearOutSlowInEasing),
                        )
                        .padding(horizontal = screenWidthDp(16.dp))
                        .padding(bottom = 28.dp),
                    label = stringResource(R.string.create_label_deadline),
                    error = uiState.deadlineError?.let { stringResource(it.message) } ?: "",
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
                    visualTransformation = dateTransformation,
                    neverCoverField = true,
                )
            }

            item {
                PotiLongTextField(
                    value = uiState.description,
                    onValueChanged = onDescriptionChanged,
                    placeholder = stringResource(R.string.create_placeholder_description),
                    modifier = Modifier
                        .animateItem(
                            placementSpec = tween(durationMillis = 120, easing = LinearOutSlowInEasing),
                        )
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
                        .animateItem(
                            placementSpec = tween(durationMillis = 120, easing = LinearOutSlowInEasing),
                        )
                        .padding(horizontal = screenWidthDp(16.dp))
                        .padding(bottom = 28.dp),
                    label = stringResource(R.string.create_label_account_number),
                    error = uiState.accountNumberError?.let { stringResource(it.message) } ?: "",
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
                )
            }

            item {
                PotiShortTextField(
                    value = uiState.bank,
                    onValueChanged = onBankChanged,
                    placeholder = stringResource(R.string.create_placeholder_bank),
                    modifier = Modifier
                        .animateItem(
                            placementSpec = tween(durationMillis = 120, easing = LinearOutSlowInEasing),
                        )
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
                    selectedMembers = uiState.selectedMembers,
                    onPriceChange = onMemberPriceChanged,
                    onEditBtnClick = onMemberEditBtnClick,
                    errorMessage = uiState.memberError?.let { stringResource(it.message) } ?: "",
                )
            }

            item {
                PotiDivider(
                    styleType = PotiDividerStyle.LARGE,
                )

                CreateDeliverySetting(
                    allDeliveries = uiState.rawDeliveries,
                    selectedDeliveries = uiState.selectedDeliveries,
                    onDeliveryClick = onDeliveryRadioBtnClick,
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
            }
        }
    }
}

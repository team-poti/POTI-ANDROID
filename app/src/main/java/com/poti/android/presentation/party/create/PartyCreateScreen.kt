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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringArrayResource
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
import com.poti.android.core.designsystem.component.display.PotiDivider
import com.poti.android.core.designsystem.component.display.PotiDividerStyle
import com.poti.android.core.designsystem.component.display.PotiErrorMessage
import com.poti.android.core.designsystem.component.field.PotiLongTextField
import com.poti.android.core.designsystem.component.field.PotiShortTextField
import com.poti.android.core.designsystem.component.modal.PotiNoticeModal
import com.poti.android.core.designsystem.component.modal.PotiSmallModal
import com.poti.android.core.designsystem.component.navigation.PotiBottomButton
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.domain.model.artist.MemberPriceOption
import com.poti.android.presentation.party.component.MemberSelectBottomSheet
import com.poti.android.presentation.party.create.component.CreateDeliverySetting
import com.poti.android.presentation.party.create.component.CreateDropdownField
import com.poti.android.presentation.party.create.component.CreateMemberSetting
import com.poti.android.presentation.party.create.component.CreatePhotoUpload
import com.poti.android.presentation.party.create.component.SellerNotice
import com.poti.android.presentation.party.create.component.ViewType
import com.poti.android.presentation.party.create.model.CreateModalType
import com.poti.android.presentation.party.create.model.CreateUiEffect.NavigateToBack
import com.poti.android.presentation.party.create.model.CreateUiEffect.NavigateToDetail
import com.poti.android.presentation.party.create.model.CreateUiEffect.NavigateToSearch
import com.poti.android.presentation.party.create.model.CreateUiIntent.OnAccountNumberChange
import com.poti.android.presentation.party.create.model.CreateUiIntent.OnAllMemberSelect
import com.poti.android.presentation.party.create.model.CreateUiIntent.OnBack
import com.poti.android.presentation.party.create.model.CreateUiIntent.OnBackConfirm
import com.poti.android.presentation.party.create.model.CreateUiIntent.OnBankChange
import com.poti.android.presentation.party.create.model.CreateUiIntent.OnCloseBottomSheet
import com.poti.android.presentation.party.create.model.CreateUiIntent.OnCloseCreateModal
import com.poti.android.presentation.party.create.model.CreateUiIntent.OnCloseDialog
import com.poti.android.presentation.party.create.model.CreateUiIntent.OnCreateClick
import com.poti.android.presentation.party.create.model.CreateUiIntent.OnCreateConfirm
import com.poti.android.presentation.party.create.model.CreateUiIntent.OnDeadlineChange
import com.poti.android.presentation.party.create.model.CreateUiIntent.OnDeliveryPriceChange
import com.poti.android.presentation.party.create.model.CreateUiIntent.OnDeliverySelect
import com.poti.android.presentation.party.create.model.CreateUiIntent.OnDescriptionChange
import com.poti.android.presentation.party.create.model.CreateUiIntent.OnImagesChanged
import com.poti.android.presentation.party.create.model.CreateUiIntent.OnMemberEditClick
import com.poti.android.presentation.party.create.model.CreateUiIntent.OnMemberPriceChange
import com.poti.android.presentation.party.create.model.CreateUiIntent.OnMemberSelect
import com.poti.android.presentation.party.create.model.CreateUiIntent.OnMemberSelectDone
import com.poti.android.presentation.party.create.model.CreateUiIntent.OnProductChange
import com.poti.android.presentation.party.create.model.CreateUiIntent.OnProductFocus
import com.poti.android.presentation.party.create.model.CreateUiIntent.OnProductSelect
import com.poti.android.presentation.party.create.model.CreateUiIntent.OnSearchClick
import com.poti.android.presentation.party.create.model.CreateUiIntent.ScrollComplete
import com.poti.android.presentation.party.create.model.CreateUiState
import com.poti.android.presentation.party.create.model.DeliveryOptionUiModel
import com.poti.android.presentation.party.create.util.DateTransformation
import kotlinx.collections.immutable.toPersistentList

@Composable
fun PartyCreateRoute(
    onPopBackStack: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    viewModel: PartyCreateViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BackHandler {
        viewModel.processIntent(OnBack)
    }

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            NavigateToBack -> {
                onPopBackStack()
            }

            NavigateToSearch -> onNavigateToSearch()

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

    when (uiState.visibleModal) {
        CreateModalType.EXIT_CONFIRM -> PotiSmallModal(
            onDismissRequest = { viewModel.processIntent(OnCloseDialog) },
            title = stringResource(R.string.create_exit_dialog_title),
            text = stringResource(R.string.create_exit_dialog_content),
            dismissBtnText = stringResource(R.string.create_exit_dialog_dismiss_text),
            confirmBtnText = stringResource(R.string.create_exit_dialog_confirm_text),
            onDismissBtnClick = { viewModel.processIntent(OnBackConfirm) },
            onConfirmBtnClick = { viewModel.processIntent(OnCloseDialog) },
        )

        CreateModalType.CREATE_COMPLETE -> PotiNoticeModal(
            title = stringResource(R.string.create_notice_title),
            subtitle = stringResource(R.string.create_complete_modal_subtitle),
            notices = stringArrayResource(R.array.create_complete_modal_notices).toPersistentList(),
            agreement = stringResource(R.string.create_complete_modal_agreement),
            confirmButtonText = stringResource(R.string.action_button_confirm),
            onDismiss = { viewModel.processIntent(OnCloseCreateModal) },
            onConfirm = { viewModel.processIntent(OnCreateConfirm) },
        )

        null -> Unit
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
        onDeliveryPriceChanged = { viewModel.processIntent(OnDeliveryPriceChange(it)) },
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
    onDeliveryRadioBtnClick: (DeliveryOptionUiModel) -> Unit,
    onDeliveryPriceChanged: (DeliveryOptionUiModel) -> Unit,
    onCreateBtnClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val dateTransformation = remember { DateTransformation() }

    var listBottom by remember { mutableFloatStateOf(0f) }

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
            modifier = Modifier
                .padding(innerPadding)
                .onGloballyPositioned { coordinates ->
                    listBottom = coordinates.boundsInWindow().bottom
                },
        ) {
            item {
                Text(
                    text = stringResource(R.string.create_label_product_info),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
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
                            .padding(horizontal = 16.dp)
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
                        .padding(horizontal = 16.dp)
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
                        .padding(horizontal = 16.dp)
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
                        .padding(horizontal = 16.dp)
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
                        .padding(horizontal = 16.dp)
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
                        .padding(horizontal = 16.dp)
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
                    layoutBottom = listBottom,
                )
            }

            item {
                PotiDivider(
                    styleType = PotiDividerStyle.LARGE,
                )

                CreateDeliverySetting(
                    deliveryOptions = uiState.deliveryOptions,
                    onDeliveryClick = onDeliveryRadioBtnClick,
                    onPriceChange = onDeliveryPriceChanged,
                    errorMessage = uiState.deliveryError?.let { stringResource(it.message) } ?: "",
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

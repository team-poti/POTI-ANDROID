package com.poti.android.presentation.party.detail

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.poti.android.R
import com.poti.android.core.common.extension.getSuccessDataOrNull
import com.poti.android.core.common.util.HandleSideEffects
import com.poti.android.core.designsystem.component.display.PotiDivider
import com.poti.android.core.designsystem.component.display.PotiDividerStyle
import com.poti.android.core.designsystem.component.display.PotiItemOptionType
import com.poti.android.core.designsystem.component.display.PotiListOptionPrice
import com.poti.android.core.designsystem.component.display.PotiListOptionPriceSize
import com.poti.android.core.designsystem.component.field.PotiShortTextField
import com.poti.android.core.designsystem.component.modal.PotiLargeModal
import com.poti.android.core.designsystem.component.modal.PotiNoticeModal
import com.poti.android.core.designsystem.component.navigation.PotiBottomButton
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.presentation.party.detail.component.ParticipantGuidelines
import com.poti.android.presentation.party.detail.component.TotalPrice
import com.poti.android.presentation.party.detail.model.PartyDetailEffect
import com.poti.android.presentation.party.detail.model.PartyDetailIntent
import com.poti.android.presentation.party.detail.model.PartyDetailUiState
import kotlinx.collections.immutable.toPersistentList

@Composable
fun PartyJoinRoute(
    onPopBackStack: () -> Unit,
    onReload: (Long) -> Unit,
    viewModel: PartyDetailViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val addressSearchLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        if (result.resultCode != Activity.RESULT_OK) return@rememberLauncherForActivityResult

        val postalCode = result.data
            ?.getStringExtra(AddressSearchActivity.EXTRA_POSTAL_CODE)
            .orEmpty()
        val address = result.data
            ?.getStringExtra(AddressSearchActivity.EXTRA_ADDRESS)
            .orEmpty()

        if (postalCode.isNotBlank() && address.isNotBlank()) {
            viewModel.processIntent(
                PartyDetailIntent.OnAddressSelected(
                    postalCode = postalCode,
                    address = address,
                ),
            )
        }
    }

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            PartyDetailEffect.NavigateBack -> onPopBackStack()
            is PartyDetailEffect.ReloadDetail -> onReload(effect.partyId)
            else -> {}
        }
    }

    if (uiState.isParticipantNoticeModalVisible) {
        PotiNoticeModal(
            title = stringResource(R.string.party_join_notice_modal_title),
            subtitle = stringResource(R.string.party_join_notice_modal_subtitle),
            notices = stringArrayResource(R.array.party_join_notice_modal_notices).toPersistentList(),
            agreement = stringResource(R.string.party_join_notice_modal_agreement),
            confirmButtonText = stringResource(R.string.action_button_confirm),
            onDismiss = { viewModel.processIntent(PartyDetailIntent.OnParticipantNoticeDismiss) },
            onConfirm = { viewModel.processIntent(PartyDetailIntent.OnParticipantNoticeConfirm) },
            modifier = modifier,
        )
    }

    if (uiState.isJoinSuccessDialogVisible) {
        PotiLargeModal(
            onDismissRequest = {},
            title = stringResource(R.string.party_join_confirm_modal_title),
            text = stringResource(R.string.party_join_confirm_modal_text),
            btnText = stringResource(R.string.action_button_next),
            onBtnClick = { viewModel.processIntent(PartyDetailIntent.OnJoinSuccessConfirm) },
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            modifier = modifier,
        ) {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.join))
            val progress by animateLottieCompositionAsState(
                composition = composition,
                iterations = LottieConstants.IterateForever,
            )

            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier
                    .padding(vertical = 36.dp)
                    .height(208.dp),
            )
        }
    }

    PartyJoinScreen(
        uiState = uiState,
        onOrderNameChange = { viewModel.processIntent(PartyDetailIntent.OnOrderNameChange(it)) },
        onAddressSearchClick = { addressSearchLauncher.launch(Intent(context, AddressSearchActivity::class.java)) },
        onDetailAddressChange = { viewModel.processIntent(PartyDetailIntent.OnDetailAddressChange(it)) },
        onContactChange = { viewModel.processIntent(PartyDetailIntent.OnContactChange(it)) },
        onBackClick = { viewModel.processIntent(PartyDetailIntent.OnBackClick) },
        onJoinClick = { viewModel.processIntent(PartyDetailIntent.OnFinalJoinClick) },
        modifier = modifier,
    )
}

@Composable
private fun PartyJoinScreen(
    uiState: PartyDetailUiState,
    onOrderNameChange: (String) -> Unit,
    onAddressSearchClick: () -> Unit,
    onDetailAddressChange: (String) -> Unit,
    onContactChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onJoinClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            PotiHeaderPage(
                onNavigationClick = onBackClick,
                title = stringResource(R.string.party_detail_title, uiState.partyDetail.getSuccessDataOrNull()?.uploader?.nickname ?: ""),
            )
        },
        bottomBar = {
            PotiBottomButton(
                text = stringResource(R.string.party_join_button),
                onClick = onJoinClick,
                enabled = uiState.isDetailJoinEnable,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 18.dp, bottom = 24.dp)
                    .padding(horizontal = 16.dp),
            ) {
                Column(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    uiState.selectedMembers.forEach { member ->
                        PotiListOptionPrice(
                            itemOptionType = PotiItemOptionType.MEMBER,
                            itemOptionText = member.option,
                            priceText = stringResource(R.string.party_option_price_won, member.price ?: "0"),
                            sizeType = PotiListOptionPriceSize.SMALL,
                        )
                    }

                    uiState.selectedDelivery?.let { delivery ->
                        PotiListOptionPrice(
                            itemOptionType = PotiItemOptionType.DELIVERY,
                            itemOptionText = delivery.option,
                            priceText = stringResource(R.string.party_option_price_won, delivery.price ?: "0"),
                            sizeType = PotiListOptionPriceSize.SMALL,
                        )
                    }
                }

                PotiDivider(
                    styleType = PotiDividerStyle.SMALL,
                    modifier = Modifier.padding(vertical = 16.dp),
                )

                TotalPrice(totalPrice = uiState.totalPrice)
            }

            PotiDivider(styleType = PotiDividerStyle.LARGE)

            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp),
            ) {
                Text(
                    text = stringResource(R.string.party_join_order_info),
                    style = PotiTheme.typography.title18sb,
                    color = PotiTheme.colors.black,
                    modifier = Modifier.padding(bottom = 24.dp),
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(28.dp),
                ) {
                    PotiShortTextField(
                        value = uiState.orderName,
                        onValueChanged = onOrderNameChange,
                        placeholder = stringResource(R.string.delivery_name_placeholder),
                        label = stringResource(R.string.delivery_name_label),
                        error = if (uiState.isOrderNameError) stringResource(R.string.delivery_name_error) else "",
                        imeAction = ImeAction.Next,
                    )

                    PotiShortTextField(
                        value = uiState.postalCode,
                        onFieldClick = onAddressSearchClick,
                        placeholder = stringResource(R.string.delivery_postal_placeholder),
                        label = stringResource(R.string.delivery_postal_label),
                        error = if (uiState.isPostalCodeError) stringResource(R.string.delivery_postal_error) else "",
                        onValueChanged = {},
                    )

                    PotiShortTextField(
                        value = uiState.address,
                        onFieldClick = onAddressSearchClick,
                        placeholder = stringResource(R.string.delivery_address_placeholder),
                        label = stringResource(R.string.delivery_address_label),
                        error = if (uiState.isAddressError) stringResource(R.string.delivery_address_error) else "",
                        onValueChanged = {},
                    )

                    PotiShortTextField(
                        value = uiState.detailAddress,
                        onValueChanged = onDetailAddressChange,
                        placeholder = stringResource(R.string.delivery_detail_address_placeholder),
                        label = stringResource(R.string.delivery_detail_address_label),
                        imeAction = ImeAction.Next,
                    )

                    PotiShortTextField(
                        value = uiState.contact,
                        onValueChanged = onContactChange,
                        placeholder = stringResource(R.string.delivery_contact_placeholder),
                        label = stringResource(R.string.delivery_contact_label),
                        error = if (uiState.isContactError) stringResource(R.string.delivery_contact_error) else "",
                        keyboardType = KeyboardType.Number,
                    )
                }
            }

            PotiDivider(styleType = PotiDividerStyle.LARGE)

            ParticipantGuidelines()
        }
    }
}

@Preview
@Composable
private fun PartyJoinScreenPreview() {
    PotiTheme {
        PartyJoinScreen(
            uiState = PartyDetailUiState(),
            onBackClick = {},
            onJoinClick = {},
            onOrderNameChange = {},
            onAddressSearchClick = {},
            onDetailAddressChange = {},
            onContactChange = {},
        )
    }
}

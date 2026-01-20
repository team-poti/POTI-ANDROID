package com.poti.android.presentation.party.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poti.android.R
import com.poti.android.core.common.extension.getSuccessDataOrNull
import com.poti.android.core.common.util.HandleSideEffects
import com.poti.android.core.common.util.screenWidthDp
import com.poti.android.core.designsystem.component.display.PotiDivider
import com.poti.android.core.designsystem.component.display.PotiDividerStyle
import com.poti.android.core.designsystem.component.display.PotiItemOptionType
import com.poti.android.core.designsystem.component.display.PotiListOptionPrice
import com.poti.android.core.designsystem.component.display.PotiListOptionPriceSize
import com.poti.android.core.designsystem.component.field.PotiShortTextField
import com.poti.android.core.designsystem.component.modal.PotiLargeModal
import com.poti.android.core.designsystem.component.navigation.PotiBottomButton
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.presentation.party.detail.component.ParticipantGuidelines
import com.poti.android.presentation.party.detail.component.TotalPrice
import com.poti.android.presentation.party.detail.model.PartyDetailEffect
import com.poti.android.presentation.party.detail.model.PartyDetailIntent
import com.poti.android.presentation.party.detail.model.PartyDetailUiState

@Composable
fun PartyJoinRoute(
    onPopBackStack: () -> Unit,
    onReload: (Long) -> Unit,
    viewModel: PartyDetailViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            PartyDetailEffect.NavigateBack -> onPopBackStack()
            is PartyDetailEffect.ReloadDetail -> onReload(effect.partyId)
            else -> {}
        }
    }

    if (uiState.isJoinSuccessDialogVisible) {
        PotiLargeModal(
            onDismissRequest = {},
            title = stringResource(R.string.party_join_confirm_modal_title),
            text = stringResource(R.string.party_join_confirm_modal_text),
            btnText = stringResource(R.string.action_button_next),
            onBtnClick = { viewModel.processIntent(PartyDetailIntent.OnJoinSuccessConfirm) },
            image = R.drawable.ic_launcher_background,
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            modifier = modifier,
        )
    }

    PartyJoinScreen(
        uiState = uiState,
        onOrderNameChange = { viewModel.processIntent(PartyDetailIntent.OnOrderNameChange(it)) },
        onPostalCodeChange = { viewModel.processIntent(PartyDetailIntent.OnPostalCodeChange(it)) },
        onAddressChange = { viewModel.processIntent(PartyDetailIntent.OnAddressChange(it)) },
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
    onPostalCodeChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
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
                title = stringResource(R.string.party_detail_title, uiState.partyDetail.getSuccessDataOrNull()?.userSummary?.nickname ?: ""),
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
                    .padding(horizontal = screenWidthDp(16.dp)),
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
                modifier = Modifier.padding(horizontal = screenWidthDp(16.dp), vertical = 24.dp),
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
                        placeholder = stringResource(R.string.party_join_order_name_placeholder),
                        label = stringResource(R.string.field_label_name),
                        error = if (uiState.isOrderNameError) stringResource(R.string.party_join_order_name_error) else "",
                    )

                    PotiShortTextField(
                        value = uiState.postalCode,
                        onValueChanged = { onPostalCodeChange(it.take(5)) },
                        placeholder = stringResource(R.string.party_join_order_postal_placeholder),
                        label = stringResource(R.string.party_join_order_postal_label),
                        error = if (uiState.isPostalCodeError) stringResource(R.string.party_join_order_postal_error) else "",
                        keyboardType = KeyboardType.Number,
                    )

                    PotiShortTextField(
                        value = uiState.address,
                        onValueChanged = onAddressChange,
                        placeholder = stringResource(R.string.party_join_order_address_placeholder),
                        label = stringResource(R.string.party_join_order_address_label),
                        error = if (uiState.isAddressError) stringResource(R.string.party_join_order_address_error) else "",
                    )

                    PotiShortTextField(
                        value = uiState.contact,
                        onValueChanged = onContactChange,
                        placeholder = stringResource(R.string.party_join_order_contact_placeholder),
                        label = stringResource(R.string.party_join_order_contact_label),
                        error = if (uiState.isContactError) stringResource(R.string.party_join_order_contact_error) else "",
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
            onPostalCodeChange = {},
            onAddressChange = {},
            onContactChange = {},
        )
    }
}

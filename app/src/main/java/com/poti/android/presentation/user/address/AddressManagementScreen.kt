package com.poti.android.presentation.user.address

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poti.android.R
import com.poti.android.core.common.util.HandleSideEffects
import com.poti.android.core.designsystem.component.button.ActionButtonType
import com.poti.android.core.designsystem.component.button.PotiActionButton
import com.poti.android.core.designsystem.component.field.PotiShortTextField
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.presentation.party.detail.AddressSearchActivity
import com.poti.android.presentation.user.address.model.AddressManagementUiEffect
import com.poti.android.presentation.user.address.model.AddressManagementUiIntent
import com.poti.android.presentation.user.address.model.AddressManagementUiState

@Composable
fun AddressManagementRoute(
    onPopBackStack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddressManagementViewModel = hiltViewModel(),
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
                AddressManagementUiIntent.OnAddressSelected(
                    postalCode = postalCode,
                    address = address,
                ),
            )
        }
    }

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            AddressManagementUiEffect.NavigateBack -> onPopBackStack()
            AddressManagementUiEffect.OpenAddressSearch -> {
                addressSearchLauncher.launch(Intent(context, AddressSearchActivity::class.java))
            }
        }
    }

    AddressManagementScreen(
        uiState = uiState,
        onBackClick = { viewModel.processIntent(AddressManagementUiIntent.OnBackClick) },
        onReceiverNameChange = { viewModel.processIntent(AddressManagementUiIntent.OnReceiverNameChange(it)) },
        onAddressSearchClick = { viewModel.processIntent(AddressManagementUiIntent.OnAddressSearchClick) },
        onDetailAddressChange = { viewModel.processIntent(AddressManagementUiIntent.OnDetailAddressChange(it)) },
        onContactChange = { viewModel.processIntent(AddressManagementUiIntent.OnContactChange(it)) },
        onSaveClick = { viewModel.processIntent(AddressManagementUiIntent.OnSaveClick) },
        modifier = modifier,
    )
}

@Composable
private fun AddressManagementScreen(
    uiState: AddressManagementUiState,
    onBackClick: () -> Unit,
    onReceiverNameChange: (String) -> Unit,
    onAddressSearchClick: () -> Unit,
    onDetailAddressChange: (String) -> Unit,
    onContactChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PotiTheme.colors.white),
    ) {
        PotiHeaderPage(
            onNavigationClick = onBackClick,
            title = stringResource(R.string.address_management_title),
            containerColor = PotiTheme.colors.white,
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp),
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(28.dp),
                ) {
                    PotiShortTextField(
                        value = uiState.receiverName,
                        onValueChanged = onReceiverNameChange,
                        placeholder = stringResource(R.string.delivery_name_placeholder),
                        label = stringResource(R.string.delivery_name_label),
                        error = if (uiState.isReceiverNameError) stringResource(R.string.delivery_name_error) else "",
                        imeAction = ImeAction.Next,
                    )

                    PotiShortTextField(
                        value = uiState.postalCode,
                        onFieldClick = onAddressSearchClick,
                        placeholder = stringResource(R.string.delivery_postal_placeholder),
                        label = stringResource(R.string.delivery_postal_label),
                        error = if (uiState.isPostalCodeError) stringResource(R.string.delivery_postal_error) else "",
                        onValueChanged = {},
                        trailingIcon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_search),
                                contentDescription = null,
                                tint = PotiTheme.colors.gray700,
                                modifier = Modifier.size(24.dp),
                            )
                        },
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

            PotiActionButton(
                text = stringResource(R.string.action_button_save),
                onClick = onSaveClick,
                enabled = uiState.isSaveEnabled,
                type = ActionButtonType.SECONDARY_MAIN,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 4.dp, bottom = 14.dp)
                    .fillMaxWidth(),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AddressManagementScreenPreview() {
    AddressManagementScreen(
        uiState = AddressManagementUiState(),
        onBackClick = {},
        onReceiverNameChange = {},
        onAddressSearchClick = {},
        onDetailAddressChange = {},
        onContactChange = {},
        onSaveClick = {},
    )
}

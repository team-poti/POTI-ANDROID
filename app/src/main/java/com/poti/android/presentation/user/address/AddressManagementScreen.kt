package com.poti.android.presentation.user.address

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.designsystem.component.button.ActionButtonType
import com.poti.android.core.designsystem.component.button.PotiActionButton
import com.poti.android.core.designsystem.component.field.PotiShortTextField
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage
import com.poti.android.core.designsystem.theme.PotiTheme

@Composable
fun AddressManagementRoute(
    onPopBackStack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AddressManagementScreen(
        onBackClick = onPopBackStack,
        modifier = modifier,
    )
}

@Composable
private fun AddressManagementScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        PotiHeaderPage(
            onNavigationClick = onBackClick,
            title = stringResource(R.string.address_management_title),
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
                        value = "",
                        onValueChanged = {},
                        placeholder = stringResource(R.string.delivery_name_placeholder),
                        label = stringResource(R.string.delivery_name_label),
                        error = "",
                        imeAction = ImeAction.Next,
                    )

                    PotiShortTextField(
                        value = "",
                        onFieldClick = {},
                        placeholder = stringResource(R.string.delivery_postal_placeholder),
                        label = stringResource(R.string.delivery_postal_label),
                        error = "",
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
                        value = "",
                        onFieldClick = {},
                        placeholder = stringResource(R.string.delivery_address_placeholder),
                        label = stringResource(R.string.delivery_address_label),
                        error = "",
                        onValueChanged = {},
                    )

                    PotiShortTextField(
                        value = "",
                        onValueChanged = {},
                        placeholder = stringResource(R.string.delivery_detail_address_placeholder),
                        label = stringResource(R.string.delivery_detail_address_label),
                        imeAction = ImeAction.Next,
                    )

                    PotiShortTextField(
                        value = "",
                        onValueChanged = {},
                        placeholder = stringResource(R.string.delivery_contact_placeholder),
                        label = stringResource(R.string.delivery_contact_label),
                        error = "",
                        keyboardType = KeyboardType.Number,
                    )
                }
            }

            PotiActionButton(
                text = stringResource(R.string.action_button_save),
                onClick = {},
                type = ActionButtonType.DEACTIVE_MAIN,
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
        onBackClick = {},
    )
}

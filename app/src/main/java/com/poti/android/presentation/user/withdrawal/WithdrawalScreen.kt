package com.poti.android.presentation.user.withdrawal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.designsystem.component.button.ActionButtonType
import com.poti.android.core.designsystem.component.button.PotiActionButton
import com.poti.android.core.designsystem.component.display.PotiListRadio
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage
import com.poti.android.core.designsystem.theme.PotiTheme
import kotlinx.collections.immutable.toImmutableList

@Composable
fun WithdrawalRoute(modifier: Modifier = Modifier) {
}

@Composable
fun WithdrawalScreen(
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        PotiHeaderPage(
            onNavigationClick = {},
            title = stringResource(R.string.withdrawal_title),
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
                Text(
                    text = stringResource(R.string.withdrawal_reason_label),
                    style = PotiTheme.typography.body14m,
                    color = PotiTheme.colors.gray800,
                    modifier = Modifier.padding(top = 12.dp, bottom = 8.dp),
                )

                PotiListRadio(
                    options = stringArrayResource(R.array.withdrawal_reason_options).toImmutableList(),
                    selectedOptionIndex = 1,
                    onClick = {},
                )
            }

            PotiActionButton(
                text = stringResource(R.string.withdrawal_button),
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
private fun WithdrawalScreenPreview() {
    WithdrawalScreen()
}

package com.poti.android.presentation.user.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poti.android.R
import com.poti.android.core.common.extension.onSuccess
import com.poti.android.core.common.util.HandleSideEffects
import com.poti.android.core.designsystem.component.button.PotiMenuButton
import com.poti.android.core.designsystem.component.display.PotiDivider
import com.poti.android.core.designsystem.component.display.PotiDividerStyle
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage
import com.poti.android.domain.model.auth.SocialType
import com.poti.android.domain.model.user.UserAccount
import com.poti.android.presentation.user.account.model.AccountSettingUiEffect
import com.poti.android.presentation.user.account.model.AccountSettingUiIntent

@Composable
fun AccountSettingRoute(
    onPopBackStack: () -> Unit,
    onNavigateToWithdrawal: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AccountSettingViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            AccountSettingUiEffect.NavigateBack -> onPopBackStack()
            AccountSettingUiEffect.NavigateToWithdrawal -> onNavigateToWithdrawal()
        }
    }

    uiState.userAccountLoadState.onSuccess { userAccount ->
        AccountSettingScreen(
            userAccount = userAccount,
            onBackClick = { viewModel.processIntent(AccountSettingUiIntent.OnBackClick) },
            onLogoutClick = { viewModel.processIntent(AccountSettingUiIntent.OnLogoutClick) },
            onWithdrawalClick = { viewModel.processIntent(AccountSettingUiIntent.OnWithdrawalClick) },
            modifier = modifier,
        )
    }
}

@Composable
private fun AccountSettingScreen(
    userAccount: UserAccount,
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onWithdrawalClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    val socialTypeText = when (userAccount.socialType) {
        SocialType.KAKAO -> stringResource(R.string.social_type_kakao)
        SocialType.GOOGLE -> stringResource(R.string.social_type_google)
    }

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        PotiHeaderPage(
            onNavigationClick = onBackClick,
            title = stringResource(R.string.account_setting_title),
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            PotiMenuButton(
                text = stringResource(R.string.account_name),
                onClick = {},
                modifier = Modifier.padding(horizontal = 8.dp),
                trailingText = userAccount.nickname,
            )

            PotiMenuButton(
                text = stringResource(R.string.account_email),
                onClick = {},
                modifier = Modifier.padding(horizontal = 8.dp),
                trailingText = userAccount.email,
            )

            PotiMenuButton(
                text = stringResource(R.string.social_account),
                onClick = {},
                modifier = Modifier.padding(horizontal = 8.dp),
                trailingText = socialTypeText,
            )

            PotiDivider(
                PotiDividerStyle.LARGE,
                modifier = Modifier.padding(vertical = 8.dp),
            )

            PotiMenuButton(
                text = stringResource(R.string.account_logout),
                onClick = onLogoutClick,
                modifier = Modifier.padding(horizontal = 8.dp),
            )

            PotiMenuButton(
                text = stringResource(R.string.account_withdrawal_menu),
                onClick = onWithdrawalClick,
                modifier = Modifier.padding(horizontal = 8.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AccountSettingScreenPreview() {
    AccountSettingScreen(
        userAccount = UserAccount(
            nickname = "포티공주",
            email = "poti@example.com",
            socialType = SocialType.KAKAO,
        ),
        onBackClick = {},
        onLogoutClick = {},
        onWithdrawalClick = {},
    )
}

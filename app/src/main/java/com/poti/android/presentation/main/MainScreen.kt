package com.poti.android.presentation.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.poti.android.R
import com.poti.android.core.auth.SocialLoginLauncher
import com.poti.android.core.designsystem.component.modal.PotiSmallModal
import com.poti.android.core.navigation.Route
import com.poti.android.presentation.auth.navigation.navigateToLogin

@Composable
fun MainScreen(
    targetDestination: Route,
    socialLoginLauncher: SocialLoginLauncher,
    navigator: MainNavigator = rememberPotiNavigator(),
    viewModel: MainViewModel = hiltViewModel(),
    onSplashFinished: () -> Unit = {},
    onLoginSuccess: () -> Unit = {},
    onOnboardingFinished: () -> Unit = {},
) {
    var showLoginRequiredDialog by remember { mutableStateOf(false) }

    if (showLoginRequiredDialog) {
        PotiSmallModal(
            onDismissRequest = { showLoginRequiredDialog = false },
            title = stringResource(R.string.login_required_title),
            text = stringResource(R.string.login_required_history),
            dismissBtnText = stringResource(R.string.login_required_dismiss),
            confirmBtnText = stringResource(R.string.login_required_confirm),
            onDismissBtnClick = { showLoginRequiredDialog = false },
            onConfirmBtnClick = {
                showLoginRequiredDialog = false
                navigator.navController.navigateToLogin()
            },
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            MainBottomBar(
                visible = navigator.shouldShowBottomBar(),
                currentTab = navigator.currentTab,
                onTabSelected = { tab ->
                    if (tab == MainTab.HISTORY && viewModel.isGuest()) {
                        showLoginRequiredDialog = true
                    } else {
                        navigator.navigate(tab)
                    }
                },
            )
        },
    ) { innerPadding ->
        MainNavHost(
            navigator = navigator,
            targetDestination = targetDestination,
            paddingValues = innerPadding,
            socialLoginLauncher = socialLoginLauncher,
            onSplashFinished = onSplashFinished,
            onLoginSuccess = onLoginSuccess,
            onOnboardingFinished = onOnboardingFinished,
        )
    }
}

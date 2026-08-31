package com.poti.android.presentation.main

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poti.android.R
import com.poti.android.core.auth.SocialLoginLauncher
import com.poti.android.core.common.extension.openSystemNotificationSetting
import com.poti.android.core.common.util.HandleSideEffects
import com.poti.android.core.designsystem.component.modal.PotiPermissionModal
import com.poti.android.core.designsystem.component.modal.PotiSmallModal
import com.poti.android.core.navigation.Route
import com.poti.android.core.permission.PermissionRequestRoute
import com.poti.android.presentation.auth.navigation.navigateToLogin

@SuppressLint("InlinedApi")
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
    val showPermissionModal by viewModel.showPermissionModal.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val activity = LocalActivity.current

    val systemPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted -> viewModel.applySystemPermissionDialogResult(isGranted) }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.syncSystemNotificationPermission()
    }

    HandleSideEffects(viewModel.permissionRequestRoute) { route ->
        when (route) {
            PermissionRequestRoute.SystemDialog ->
                systemPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)

            PermissionRequestRoute.SystemSetting -> context.openSystemNotificationSetting()
            PermissionRequestRoute.AlreadyGranted -> Unit
        }
    }

    if (showPermissionModal) {
        PotiPermissionModal(
            onDismiss = { viewModel.dismissPermissionModal() },
            onAllowClick = {
                viewModel.allowNotificationPermission(
                    shouldShowRationale = activity?.let {
                        ActivityCompat.shouldShowRequestPermissionRationale(
                            it,
                            Manifest.permission.POST_NOTIFICATIONS,
                        )
                    } == true,
                )
            },
        )
    }

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

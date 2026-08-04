package com.poti.android.presentation.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.poti.android.core.auth.SocialLoginLauncher
import com.poti.android.core.navigation.Route

@Composable
fun MainScreen(
    targetDestination: Route,
    socialLoginLauncher: SocialLoginLauncher,
    navigator: MainNavigator = rememberPotiNavigator(),
    onSplashFinished: () -> Unit = {},
    onLoginSuccess: () -> Unit = {},
    onOnboardingFinished: () -> Unit = {},
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            MainBottomBar(
                visible = navigator.shouldShowBottomBar(),
                currentTab = navigator.currentTab,
                onTabSelected = { navigator.navigate(it) },
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

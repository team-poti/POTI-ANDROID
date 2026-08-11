package com.poti.android.presentation.main

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.poti.android.core.auth.SocialLoginLauncher
import com.poti.android.core.navigation.Route
import com.poti.android.presentation.auth.navigation.authNavGraph
import com.poti.android.presentation.history.navigation.historyNavGraph
import com.poti.android.presentation.onboarding.navigation.onboardingNavGraph
import com.poti.android.presentation.party.partyNavGraph
import com.poti.android.presentation.splash.SplashRoute
import com.poti.android.presentation.splash.splashNavGraph
import com.poti.android.presentation.user.mypage.navigation.myPageNavGraph
import com.poti.android.presentation.user.profile.navigation.profileNavGraph

private const val DURATION = 160

@Composable
fun MainNavHost(
    navigator: MainNavigator,
    targetDestination: Route,
    paddingValues: PaddingValues,
    socialLoginLauncher: SocialLoginLauncher,
    modifier: Modifier = Modifier,
    onSplashFinished: () -> Unit = {},
    onLoginSuccess: () -> Unit = {},
    onOnboardingFinished: () -> Unit = {},
) {
    NavHost(
        navController = navigator.navController,
        startDestination = SplashRoute,
        modifier = modifier.fillMaxSize(),
        enterTransition = { fadeIn(tween(DURATION)) },
        exitTransition = { fadeOut(tween(DURATION)) },
        popEnterTransition = { fadeIn(tween(DURATION)) },
        popExitTransition = { fadeOut(tween(DURATION)) },
    ) {
        splashNavGraph(
            navController = navigator.navController,
            destination = targetDestination,
            onNavigated = onSplashFinished,
        )
        authNavGraph(
            navController = navigator.navController,
            onNavigateToHome = onLoginSuccess,
            socialLoginLauncher = socialLoginLauncher,
        )
        onboardingNavGraph(
            navController = navigator.navController,
            paddingValues = paddingValues,
            onNavigateToHome = onOnboardingFinished,
        )
        partyNavGraph(
            navController = navigator.navController,
            paddingValues = paddingValues,
        )
        historyNavGraph(
            navController = navigator.navController,
            paddingValues = paddingValues,
        )
        myPageNavGraph(
            navController = navigator.navController,
            paddingValues = paddingValues,
        )
        profileNavGraph(
            paddingValues = paddingValues,
            onPopBackStack = navigator.navController::popBackStack,
        )
    }
}

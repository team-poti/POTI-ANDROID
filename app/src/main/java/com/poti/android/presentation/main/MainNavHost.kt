package com.poti.android.presentation.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.poti.android.core.navigation.Route
import com.poti.android.presentation.auth.navigation.authNavGraph
import com.poti.android.presentation.history.navigation.historyNavGraph
import com.poti.android.presentation.onboarding.navigation.onboardingNavGraph
import com.poti.android.presentation.party.partyNavGraph
import com.poti.android.presentation.splash.SplashRoute
import com.poti.android.presentation.splash.splashNavGraph
import com.poti.android.presentation.user.mypage.navigation.myPageNavGraph
import com.poti.android.presentation.user.profile.navigation.profileNavGraph

@Composable
fun MainNavHost(
    navigator: MainNavigator,
    targetDestination: Route,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navigator.navController,
        startDestination = SplashRoute,
        modifier = modifier.fillMaxSize(),
    ) {
        splashNavGraph(
            navController = navigator.navController,
            destination = targetDestination,
        )
        authNavGraph(
            navController = navigator.navController,
            onNavigateToHome = navigator::navigateToHome,
        )
        onboardingNavGraph(
            navController = navigator.navController,
            paddingValues = paddingValues,
            onNavigateToHome = navigator::navigateToHome,
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

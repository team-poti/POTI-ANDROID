package com.poti.android.presentation.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.poti.android.core.navigation.Route
import com.poti.android.presentation.auth.navigation.authNavGraph
import com.poti.android.presentation.history.navigation.historyNavGraph
import com.poti.android.presentation.onboarding.navigation.navigateToOnboardingGuide
import com.poti.android.presentation.onboarding.navigation.onboardingNavGraph
import com.poti.android.presentation.party.partyNavGraph
import com.poti.android.presentation.user.mypage.navigation.myPageNavGraph
import com.poti.android.presentation.user.profile.navigation.profileNavGraph

@Composable
fun MainNavHost(
    navigator: MainNavigator,
    startDestination: Route,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navigator.navController,
        startDestination = startDestination,
        modifier = modifier.fillMaxSize(),
    ) {
        authNavGraph(
            onNavigateToOnboarding = navigator.navController::navigateToOnboardingGuide,
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
            paddingValues = paddingValues,
        )
        profileNavGraph(
            paddingValues = paddingValues,
            onPopBackStack = navigator.navController::popBackStack,
        )
    }
}

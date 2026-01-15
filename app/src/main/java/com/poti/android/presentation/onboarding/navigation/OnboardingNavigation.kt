package com.poti.android.presentation.onboarding.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.poti.android.core.navigation.Route
import com.poti.android.presentation.onboarding.OnboardingArtistRoute
import com.poti.android.presentation.onboarding.OnboardingGuideRoute
import com.poti.android.presentation.onboarding.OnboardingNicknameRoute
import com.poti.android.presentation.party.home.navigation.navigateToHome
import kotlinx.serialization.Serializable

sealed interface OnboardingRoute : Route {
    @Serializable
    data object Guide : OnboardingRoute

    @Serializable
    data object Nickname : OnboardingRoute

    @Serializable
    data object Artist : OnboardingRoute
}

fun NavController.navigateToOnboardingGuide() {
    navigate(OnboardingRoute.Guide)
}

fun NavController.navigateToOnboardingNickname() {
    navigate(OnboardingRoute.Nickname)
}

fun NavController.navigateToOnboardingArtist() {
    navigate(OnboardingRoute.Artist)
}

fun NavGraphBuilder.onboardingNavGraph(
    navController: NavController,
    paddingValues: PaddingValues,
) {
    composable<OnboardingRoute.Guide> {
        OnboardingGuideRoute(
            onPopBackStack = navController::popBackStack,
            onNavigateToOnboardingNickname = navController::navigateToOnboardingNickname,
            modifier = Modifier.padding(paddingValues),
        )
    }
    composable<OnboardingRoute.Nickname> {
        OnboardingNicknameRoute(
            onPopBackStack = navController::popBackStack,
            onNavigateToOnboardingArtist = navController::navigateToOnboardingArtist,
            modifier = Modifier.padding(paddingValues),
        )
    }
    composable<OnboardingRoute.Artist> {
        OnboardingArtistRoute(
            onPopBackStack = navController::popBackStack,
            onNavigateToHome = navController::navigateToHome,
            modifier = Modifier.padding(paddingValues),
        )
    }
}

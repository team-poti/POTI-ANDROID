package com.poti.android.presentation.onboarding.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import com.poti.android.core.common.extension.sharedViewModel
import com.poti.android.core.common.extension.slideComposable
import com.poti.android.core.navigation.Route
import com.poti.android.presentation.onboarding.OnboardingArtistRoute
import com.poti.android.presentation.onboarding.OnboardingGuideRoute
import com.poti.android.presentation.onboarding.OnboardingNicknameRoute
import kotlinx.serialization.Serializable

@Serializable
object OnboardingGraph : Route

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
    onNavigateToHome: () -> Unit,
) {
    navigation<OnboardingGraph>(
        startDestination = OnboardingRoute.Guide,
    ) {
        slideComposable<OnboardingRoute.Guide> { entry ->
            OnboardingGuideRoute(
                onPopBackStack = navController::popBackStack,
                onNavigateToOnboardingNickname = navController::navigateToOnboardingNickname,
                viewModel = entry.sharedViewModel(navController),
                modifier = Modifier.padding(paddingValues),
            )
        }
        slideComposable<OnboardingRoute.Nickname> { entry ->
            OnboardingNicknameRoute(
                onPopBackStack = navController::popBackStack,
                onNavigateToOnboardingArtist = navController::navigateToOnboardingArtist,
                viewModel = entry.sharedViewModel(navController),
                modifier = Modifier.padding(paddingValues),
            )
        }
        slideComposable<OnboardingRoute.Artist> { entry ->
            OnboardingArtistRoute(
                onPopBackStack = navController::popBackStack,
                onNavigateToHome = onNavigateToHome,
                viewModel = entry.sharedViewModel(navController),
                modifier = Modifier.padding(paddingValues),
            )
        }
    }
}

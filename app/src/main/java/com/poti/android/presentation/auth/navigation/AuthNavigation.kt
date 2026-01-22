package com.poti.android.presentation.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.poti.android.core.navigation.Route
import com.poti.android.presentation.auth.LoginRoute
import com.poti.android.presentation.onboarding.navigation.navigateToOnboardingGuide
import kotlinx.serialization.Serializable

sealed interface AuthRoute : Route {
    @Serializable
    data object Login : AuthRoute
}

fun NavGraphBuilder.authNavGraph(
    navController: NavController,
    onNavigateToHome: () -> Unit,
) {
    composable<AuthRoute.Login> {
        LoginRoute(
            onNavigateToOnboarding = navController::navigateToOnboardingGuide,
            onNavigateToHome = onNavigateToHome,
        )
    }
}

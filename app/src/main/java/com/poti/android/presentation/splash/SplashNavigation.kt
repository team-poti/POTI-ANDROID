package com.poti.android.presentation.splash

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.poti.android.core.navigation.Route
import kotlinx.serialization.Serializable

@Serializable
data object SplashRoute : Route

fun NavController.navigateToSplash() {
    navigate(SplashRoute)
}

fun NavGraphBuilder.splashNavGraph(
    navController: NavController,
    destination: Route,
) {
    composable<SplashRoute> {
        SplashRoute(
            onAnimationFinished = {
                navController.navigate(destination) {
                    popUpTo(SplashRoute) { inclusive = true }
                    launchSingleTop = true
                }
            },
        )
    }
}

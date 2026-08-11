package com.poti.android.presentation.splash

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.poti.android.core.navigation.Route
import kotlinx.serialization.Serializable

@Serializable
data object SplashRoute : Route

fun NavGraphBuilder.splashNavGraph(
    navController: NavController,
    destination: Route,
    onNavigated: () -> Unit = {},
) {
    composable<SplashRoute> {
        SplashRoute(
            onAnimationFinished = {
                navController.navigate(destination) {
                    popUpTo(SplashRoute) { inclusive = true }
                    launchSingleTop = true
                }
                onNavigated()
            },
        )
    }
}

package com.poti.android.presentation.feature.splash.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.poti.android.presentation.feature.splash.SplashRoute
import com.poti.android.presentation.navigation.Route.Splash

fun NavGraphBuilder.splashNavGraph() {
    composable<Splash> {
        SplashRoute()
    }
}

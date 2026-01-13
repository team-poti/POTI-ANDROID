package com.poti.android.presentation.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.poti.android.core.navigation.Route
import com.poti.android.presentation.auth.LoginRoute
import kotlinx.serialization.Serializable

sealed interface AuthRoute : Route {
    @Serializable
    data object Login : AuthRoute
}

fun NavGraphBuilder.authNavGraph() {
    composable<AuthRoute.Login> {
        LoginRoute()
    }
}

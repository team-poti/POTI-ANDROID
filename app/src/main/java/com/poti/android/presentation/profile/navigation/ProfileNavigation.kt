package com.poti.android.presentation.profile.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.poti.android.core.navigation.Route
import kotlinx.serialization.Serializable

sealed interface ProfileRoute : Route {
    @Serializable
    data object Profile : ProfileRoute
}

fun NavGraphBuilder.profileNavGraph(
    paddingValues: PaddingValues,
) {
    composable<ProfileRoute.Profile> { }
}

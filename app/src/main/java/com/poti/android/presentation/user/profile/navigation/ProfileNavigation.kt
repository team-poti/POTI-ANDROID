package com.poti.android.presentation.user.profile.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.poti.android.core.navigation.Route
import com.poti.android.presentation.user.profile.ProfileScreenRoute
import kotlinx.serialization.Serializable

sealed interface ProfileRoute : Route {
    @Serializable
    data object Profile : ProfileRoute
}

fun NavController.navigateToProfile() {
    navigate(ProfileRoute.Profile)
}

fun NavGraphBuilder.profileNavGraph(
    paddingValues: PaddingValues,
    onBackClick: () -> Unit,
) {
    composable<ProfileRoute.Profile> {
        ProfileScreenRoute(
            onPopBackStack = onBackClick,
            modifier = Modifier.padding(paddingValues),
        )
    }
}

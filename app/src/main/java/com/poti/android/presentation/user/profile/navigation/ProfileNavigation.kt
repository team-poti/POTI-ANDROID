package com.poti.android.presentation.user.profile.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.poti.android.core.common.extension.slideComposable
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.core.navigation.Route
import com.poti.android.presentation.user.profile.ProfileScreenRoute
import kotlinx.serialization.Serializable

sealed interface ProfileRoute : Route {
    @Serializable
    data class Profile(val userId: Long) : ProfileRoute
}

fun NavController.navigateToProfile(userId: Long) {
    navigate(ProfileRoute.Profile(userId))
}

fun NavGraphBuilder.profileNavGraph(
    paddingValues: PaddingValues,
    onPopBackStack: () -> Unit,
) {
    slideComposable<ProfileRoute.Profile> {
        ProfileScreenRoute(
            onPopBackStack = onPopBackStack,
            modifier = Modifier
                .fillMaxSize()
                .background(PotiTheme.colors.gray100)
                .padding(paddingValues),
        )
    }
}

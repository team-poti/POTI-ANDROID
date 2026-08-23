package com.poti.android.presentation.user.editprofile.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.poti.android.core.common.extension.slideComposable
import com.poti.android.core.navigation.Route
import com.poti.android.presentation.user.editprofile.EditProfileRoute
import kotlinx.serialization.Serializable

sealed interface EditProfileRoute : Route {
    @Serializable
    data object EditProfile : EditProfileRoute
}

fun NavController.navigateToEditProfile() {
    navigate(EditProfileRoute.EditProfile)
}

fun NavGraphBuilder.editProfileNavGraph(
    navController: NavController,
    paddingValues: PaddingValues,
) {
    slideComposable<EditProfileRoute.EditProfile> {
        EditProfileRoute(
            onPopBackStack = navController::popBackStack,
            modifier = Modifier.padding(paddingValues),
        )
    }
}

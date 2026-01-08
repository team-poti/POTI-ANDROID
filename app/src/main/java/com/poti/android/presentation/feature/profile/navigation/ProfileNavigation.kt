package com.poti.android.presentation.feature.profile.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.poti.android.core.navigation.ProfileRoute

fun NavGraphBuilder.profileNavGraph(
    paddingValues: PaddingValues,
) {
    composable<ProfileRoute.Profile> { }
}

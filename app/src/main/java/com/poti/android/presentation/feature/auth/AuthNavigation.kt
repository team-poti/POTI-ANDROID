package com.poti.android.presentation.feature.auth

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.poti.android.core.navigation.AuthRoute

fun NavGraphBuilder.authNavGraph(
    paddingValues: PaddingValues,
) {
    composable<AuthRoute.Login> {
        LoginRoute(modifier = Modifier.padding(paddingValues))
    }
}

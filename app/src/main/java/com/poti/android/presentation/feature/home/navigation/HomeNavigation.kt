package com.poti.android.presentation.feature.home.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.poti.android.core.navigation.HomeRoute
import com.poti.android.presentation.feature.home.HomeRoute

fun NavGraphBuilder.homeNavGraph(
    paddingValues: PaddingValues,
) {
    composable<HomeRoute.Home> {
        HomeRoute(modifier = Modifier.padding(paddingValues))
    }
}

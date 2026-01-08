package com.poti.android.presentation.party.home.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.poti.android.core.navigation.Route
import com.poti.android.presentation.party.home.HomeRoute
import kotlinx.serialization.Serializable

sealed interface HomeRoute : Route {
    @Serializable
    data object Home : HomeRoute
}

fun NavGraphBuilder.homeNavGraph(
    paddingValues: PaddingValues,
    onNavigateToGoodsCategory: () -> Unit,
) {
    composable<HomeRoute.Home> {
        HomeRoute(
            onNavigateToGoodsCategory = onNavigateToGoodsCategory,
            modifier = Modifier.padding(paddingValues),
        )
    }
}

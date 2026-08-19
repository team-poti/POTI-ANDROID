package com.poti.android.presentation.party.home.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.poti.android.core.navigation.Route
import com.poti.android.presentation.party.create.navigation.navigateToPartyCreate
import com.poti.android.presentation.party.home.HomeRoute
import com.poti.android.presentation.party.product.navigation.navigateToProductCategory
import com.poti.android.presentation.party.product.navigation.navigateToProductPartyList
import com.poti.android.presentation.party.search.navigation.navigateToPartySearch
import kotlinx.serialization.Serializable

sealed interface HomeRoute : Route {
    @Serializable
    data object Home : HomeRoute
}

fun NavGraphBuilder.homeNavGraph(
    paddingValues: PaddingValues,
    navController: NavController,
) {
    composable<HomeRoute.Home> {
        HomeRoute(
            onNavigateToPartySearch = navController::navigateToPartySearch,
            onNavigateToPartyCreate = navController::navigateToPartyCreate,
            onNavigateToGoodsPartyList = navController::navigateToProductPartyList,
            onNavigateToProductCategory = navController::navigateToProductCategory,
            modifier = Modifier.padding(paddingValues),
        )
    }
}

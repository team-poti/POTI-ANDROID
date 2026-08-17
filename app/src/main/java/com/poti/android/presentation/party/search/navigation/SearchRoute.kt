package com.poti.android.presentation.party.search.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.poti.android.core.common.extension.slideComposable
import com.poti.android.core.navigation.Route
import com.poti.android.presentation.party.product.navigation.navigateToProductPartyList
import com.poti.android.presentation.party.search.PartySearchRoute
import kotlinx.serialization.Serializable

sealed interface SearchRoute : Route {
    @Serializable
    data object Search : SearchRoute
}

fun NavController.navigateToPartySearch() {
    navigate(SearchRoute.Search)
}

fun NavGraphBuilder.searchNavGraph(
    navController: NavController,
    paddingValues: PaddingValues,
) {
    slideComposable<SearchRoute.Search> {
        PartySearchRoute(
            onBackClick = navController::popBackStack,
            onNavigateToProductPartyList = navController::navigateToProductPartyList,
            modifier = Modifier.padding(paddingValues),
        )
    }
}

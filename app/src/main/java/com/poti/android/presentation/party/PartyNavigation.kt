package com.poti.android.presentation.party

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.poti.android.core.navigation.Route
import com.poti.android.presentation.party.create.navigation.partyCreateNavGraph
import com.poti.android.presentation.party.detail.navigation.partyDetailNavGraph
import com.poti.android.presentation.party.goodsfilter.navigation.goodsFilterNavGraph
import com.poti.android.presentation.party.goodsfilter.navigation.navigateToGoodsCategory
import com.poti.android.presentation.party.home.navigation.HomeRoute
import com.poti.android.presentation.party.home.navigation.homeNavGraph
import kotlinx.serialization.Serializable

@Serializable
object PartyGraph : Route

fun NavGraphBuilder.partyNavGraph(
    navController: NavController,
    paddingValues: PaddingValues,
) {
    navigation<PartyGraph>(
        startDestination = HomeRoute.Home,
    ) {
        homeNavGraph(
            paddingValues = paddingValues,
            onNavigateToGoodsCategory = navController::navigateToGoodsCategory,
        )
        goodsFilterNavGraph(
            paddingValues = paddingValues,
        )
        partyDetailNavGraph(
            paddingValues = paddingValues,
            onBackClick = navController::popBackStack,
        )
        partyCreateNavGraph(
            paddingValues = paddingValues,
        )
    }
}

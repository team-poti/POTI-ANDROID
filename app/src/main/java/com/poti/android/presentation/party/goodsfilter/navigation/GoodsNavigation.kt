package com.poti.android.presentation.party.goodsfilter.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.poti.android.core.navigation.Route
import com.poti.android.presentation.party.create.navigation.navigateToPartyCreate
import com.poti.android.presentation.party.goodsfilter.GoodsCategoryRoute
import com.poti.android.presentation.party.goodsfilter.GoodsFilteredPartyListRoute
import kotlinx.serialization.Serializable

sealed interface GoodsRoute : Route {
    @Serializable
    data object GoodsList : GoodsRoute

    @Serializable
    data object GoodsPartyList : GoodsRoute
}

fun NavController.navigateToGoodsList() {
    navigate(GoodsRoute.GoodsList)
}

fun NavController.navigateToGoodsPartyList() {
    navigate(GoodsRoute.GoodsPartyList)
}

fun NavController.navigateToGoodsCategory() {
    navigate(GoodsRoute.GoodsList)
}

fun NavGraphBuilder.goodsFilterNavGraph(
    paddingValues: PaddingValues,
    navController: NavController,
) {
    composable<GoodsRoute.GoodsList> {
        GoodsCategoryRoute(
            onPopBackStack = navController::popBackStack,
            onNavigateToPartyCreate = navController::navigateToPartyCreate,
            onNavigateToGoodsPartyList = navController::navigateToGoodsPartyList,
            modifier = Modifier.padding(paddingValues),
        )
    }
    composable<GoodsRoute.GoodsPartyList> {
        GoodsFilteredPartyListRoute(
            modifier = Modifier.padding(paddingValues),
        )
    }
}

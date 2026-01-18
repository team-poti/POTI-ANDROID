package com.poti.android.presentation.party.goodsfilter.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.poti.android.core.navigation.Route
import com.poti.android.presentation.party.goodsfilter.GoodsCategoryRoute
import com.poti.android.presentation.party.goodsfilter.GoodsFilteredPartyListRoute
import kotlinx.serialization.Serializable

sealed interface GoodsRoute : Route {
    @Serializable
    data object GoodsList : GoodsRoute

    @Serializable
    data class GoodsPartyList(
        val artistId: Long,
    ) : GoodsRoute
}

fun NavController.navigateToGoodsList() {
    navigate(GoodsRoute.GoodsList)
}

fun NavController.navigateToGoodsPartyList(artistId: Long) {
    navigate(GoodsRoute.GoodsPartyList(artistId))
}

fun NavController.navigateToGoodsCategory() {
    navigate(GoodsRoute.GoodsList)
}

fun NavGraphBuilder.goodsFilterNavGraph(
    paddingValues: PaddingValues,
) {
    composable<GoodsRoute.GoodsList> {
        GoodsCategoryRoute(
            modifier = Modifier.padding(paddingValues),
        )
    }
    composable<GoodsRoute.GoodsPartyList> {backStackEntry ->
        val artistId = backStackEntry.toRoute<GoodsRoute.GoodsPartyList>().artistId

        GoodsFilteredPartyListRoute(
            artistId = artistId,
            modifier = Modifier.padding(paddingValues),
        )
    }
}

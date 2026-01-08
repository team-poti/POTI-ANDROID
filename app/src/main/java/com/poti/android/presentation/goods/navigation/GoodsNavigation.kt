package com.poti.android.presentation.goods.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.poti.android.core.navigation.Route
import com.poti.android.presentation.goods.GoodsListRoute
import com.poti.android.presentation.goods.GoodsPartyListRoute
import kotlinx.serialization.Serializable

sealed interface GoodsRoute : Route {
    @Serializable
    data object GoodsList : GoodsRoute

    @Serializable
    data object GoodsPartyList : GoodsRoute
}

fun NavGraphBuilder.goodsNavGraph(
    paddingValues: PaddingValues,
) {
    composable<GoodsRoute.GoodsList> {
        GoodsListRoute(modifier = Modifier.padding(paddingValues))
    }
    composable<GoodsRoute.GoodsPartyList> {
        GoodsPartyListRoute(modifier = Modifier.padding(paddingValues))
    }
}

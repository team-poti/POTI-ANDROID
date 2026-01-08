package com.poti.android.presentation.feature.goods.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.poti.android.core.navigation.GoodsRoute
import com.poti.android.presentation.feature.goods.GoodsListRoute
import com.poti.android.presentation.feature.goods.GoodsPartyListRoute

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

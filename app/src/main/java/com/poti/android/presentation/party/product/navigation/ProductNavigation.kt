package com.poti.android.presentation.party.product.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.poti.android.core.navigation.Route
import com.poti.android.presentation.party.create.navigation.navigateToPartyCreate
import com.poti.android.presentation.party.detail.navigation.navigateToPartyDetail
import com.poti.android.presentation.party.product.partylist.ProductPartyListRoute
import com.poti.android.presentation.party.product.productcategory.ProductCategoryRoute
import kotlinx.serialization.Serializable

sealed interface ProductRoute : Route {
    @Serializable
    data class ProductPartyList(
        val artistId: Long,
        val title: String,
    ) : ProductRoute

    @Serializable
    data class ProductCategory(
        val artistId: Long,
    ) : ProductRoute
}

fun NavController.navigateToProductPartyList(
    artistId: Long,
    title: String,
) {
    navigate(ProductRoute.ProductPartyList(artistId, title))
}

fun NavController.navigateToProductCategory(artistId: Long) {
    navigate(ProductRoute.ProductCategory(artistId))
}

fun NavGraphBuilder.productNavGraph(
    paddingValues: PaddingValues,
    navController: NavController,
    onPopBackStack: () -> Unit,
) {
    composable<ProductRoute.ProductCategory> { backStackEntry ->
        val artistId = backStackEntry.toRoute<ProductRoute.ProductCategory>().artistId

        ProductCategoryRoute(
            artistId = artistId,
            onPopBackStack = navController::popBackStack,
            onNavigateToPartyCreate = navController::navigateToPartyCreate,
            onNavigateToProductPartyList = navController::navigateToProductPartyList,
            modifier = Modifier.padding(paddingValues),
        )
    }
    composable<ProductRoute.ProductPartyList> { backStackEntry ->
        val artistId = backStackEntry.toRoute<ProductRoute.ProductPartyList>().artistId

        ProductPartyListRoute(
            artistId = artistId,
            onPopBackStack = onPopBackStack,
            onNavigateToPartyCreate = navController::navigateToPartyCreate,
            onNavigateToPartyDetail = navController::navigateToPartyDetail,
            modifier = Modifier.padding(paddingValues),
        )
    }
}

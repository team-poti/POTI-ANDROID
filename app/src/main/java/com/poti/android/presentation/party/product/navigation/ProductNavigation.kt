package com.poti.android.presentation.party.product.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.toRoute
import com.poti.android.core.analytics.AnalyticsValue
import com.poti.android.core.common.extension.slideComposable
import com.poti.android.core.navigation.Route
import com.poti.android.presentation.auth.navigation.navigateToLogin
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
        val source: String = AnalyticsValue.GOODS,
    ) : ProductRoute

    @Serializable
    data class ProductCategory(
        val artistId: Long? = null,
        val isMyArtist: Boolean,
    ) : ProductRoute
}

fun NavController.navigateToProductPartyList(
    artistId: Long,
    title: String,
) {
    navigateToProductPartyList(artistId, title, AnalyticsValue.GOODS)
}

fun NavController.navigateToProductPartyList(
    artistId: Long,
    title: String,
    source: String,
) {
    navigate(ProductRoute.ProductPartyList(artistId, title, source))
}

fun NavController.navigateToProductCategory(
    artistId: Long?,
    isMyArtist: Boolean,
) {
    navigate(ProductRoute.ProductCategory(artistId, isMyArtist))
}

fun NavGraphBuilder.productNavGraph(
    paddingValues: PaddingValues,
    navController: NavController,
    onPopBackStack: () -> Unit,
) {
    slideComposable<ProductRoute.ProductCategory> { backStackEntry ->
        val artistId = backStackEntry.toRoute<ProductRoute.ProductCategory>().artistId
        ProductCategoryRoute(
            artistId = artistId,
            onPopBackStack = navController::popBackStack,
            onNavigateToPartyCreate = navController::navigateToPartyCreate,
            onNavigateToProductPartyList = navController::navigateToProductPartyList,
            onNavigateToLogin = navController::navigateToLogin,
            modifier = Modifier.padding(paddingValues),
        )
    }
    slideComposable<ProductRoute.ProductPartyList> { backStackEntry ->
        val route = backStackEntry.toRoute<ProductRoute.ProductPartyList>()

        ProductPartyListRoute(
            artistId = route.artistId,
            onPopBackStack = onPopBackStack,
            onNavigateToPartyCreate = navController::navigateToPartyCreate,
            onNavigateToPartyDetail = { partyId ->
                navController.navigateToPartyDetail(partyId, route.source)
            },
            onNavigateToLogin = navController::navigateToLogin,
            modifier = Modifier.padding(paddingValues),
        )
    }
}

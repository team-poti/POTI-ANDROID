package com.poti.android.presentation.party.product.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.toRoute
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
    navigate(ProductRoute.ProductPartyList(artistId, title))
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
        val artistId = backStackEntry.toRoute<ProductRoute.ProductPartyList>().artistId

        ProductPartyListRoute(
            artistId = artistId,
            onPopBackStack = onPopBackStack,
            onNavigateToPartyCreate = navController::navigateToPartyCreate,
            onNavigateToPartyDetail = navController::navigateToPartyDetail,
            onNavigateToLogin = navController::navigateToLogin,
            modifier = Modifier.padding(paddingValues),
        )
    }
}

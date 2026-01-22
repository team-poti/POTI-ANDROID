package com.poti.android.presentation.party.create.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.poti.android.core.common.extension.sharedViewModel
import com.poti.android.core.navigation.Route
import com.poti.android.presentation.party.create.PartyArtistSelectRoute
import com.poti.android.presentation.party.create.PartyCreateRoute
import com.poti.android.presentation.party.create.PartyCreateViewModel
import com.poti.android.presentation.party.detail.navigation.navigateToPartyDetailFromCreate
import kotlinx.serialization.Serializable

sealed interface PartyCreateRoute : Route {
    @Serializable
    data class Create(
        val artistId: Long? = null,
        val artistName: String? = null,
        val productName: String? = null,
    ) : PartyCreateRoute

    @Serializable
    data object ArtistSelect : PartyCreateRoute
}

fun NavController.navigateToPartyCreate(
    artistId: Long? = null,
    artistName: String? = null,
    productName: String? = null,
) {
    navigate(PartyCreateRoute.Create(artistId, artistName, productName))
}

fun NavController.navigateToPartyArtistSelect() {
    navigate(PartyCreateRoute.ArtistSelect)
}

fun NavController.navigateToPartyCreateFromArtistSelect() {
    navigate(PartyCreateRoute.Create(null, null, null)) {
        popUpTo(PartyCreateRoute.Create(null, null, null)) {
            inclusive = false
        }
        launchSingleTop = true
    }
}

fun NavGraphBuilder.partyCreateNavGraph(
    navController: NavController,
    paddingValues: PaddingValues,
) {
    composable<PartyCreateRoute.Create> { entry ->
        val viewModel: PartyCreateViewModel = entry.sharedViewModel(navController)
        val params = entry.toRoute<PartyCreateRoute.Create>()

        PartyCreateRoute(
            onPopBackStack = navController::popBackStack,
            onNavigateToSearch = navController::navigateToPartyArtistSelect,
            onNavigateToDetail = navController::navigateToPartyDetailFromCreate,
            viewModel = viewModel,
            modifier = Modifier.padding(paddingValues),
            artistId = params.artistId,
            artistName = params.artistName,
            productName = params.productName,
        )
    }
    composable<PartyCreateRoute.ArtistSelect> { entry ->
        val viewModel: PartyCreateViewModel = entry.sharedViewModel(navController)
        PartyArtistSelectRoute(
            onPopBackStack = navController::navigateToPartyCreateFromArtistSelect,
            viewModel = viewModel,
            modifier = Modifier.padding(paddingValues),
        )
    }
}

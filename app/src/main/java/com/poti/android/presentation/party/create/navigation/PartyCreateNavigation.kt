package com.poti.android.presentation.party.create.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.poti.android.BuildConfig
import com.poti.android.core.common.extension.sharedViewModel
import com.poti.android.core.common.extension.slideComposable
import com.poti.android.core.navigation.Route
import com.poti.android.presentation.party.create.PartyArtistSelectRoute
import com.poti.android.presentation.party.create.PartyCreateRoute
import com.poti.android.presentation.party.create.PartyCreateViewModel
import com.poti.android.presentation.party.detail.navigation.navigateToPartyDetailFromCreate
import kotlinx.serialization.Serializable

private const val PARTY_CREATE_DEEP_LINK = "${BuildConfig.DEEP_LINK_HOST}/pot-create"

@Serializable
data class PartyCreateGraph(
    val artistId: Long? = null,
    val artistName: String? = null,
    val productName: String? = null,
)

sealed interface PartyCreateRoute : Route {
    @Serializable
    data object Create : PartyCreateRoute

    @Serializable
    data object ArtistSelect : PartyCreateRoute
}

fun NavController.navigateToPartyCreate(
    artistId: Long? = null,
    artistName: String? = null,
    productName: String? = null,
) {
    navigate(PartyCreateGraph(artistId, artistName, productName))
}

fun NavController.navigateToPartyArtistSelect() {
    navigate(PartyCreateRoute.ArtistSelect)
}

fun NavGraphBuilder.partyCreateNavGraph(
    navController: NavController,
    paddingValues: PaddingValues,
) {
    navigation<PartyCreateGraph>(
        startDestination = PartyCreateRoute.Create,
    ) {
        slideComposable<PartyCreateRoute.Create>(
            deepLinks = listOf(
                navDeepLink<PartyCreateRoute.Create>(PARTY_CREATE_DEEP_LINK),
            ),
        ) { entry ->
            val viewModel: PartyCreateViewModel = entry.sharedViewModel(navController)

            PartyCreateRoute(
                onPopBackStack = navController::popBackStack,
                onNavigateToSearch = navController::navigateToPartyArtistSelect,
                onNavigateToDetail = navController::navigateToPartyDetailFromCreate,
                viewModel = viewModel,
                modifier = Modifier.padding(paddingValues),
            )
        }
        slideComposable<PartyCreateRoute.ArtistSelect> { entry ->
            val viewModel: PartyCreateViewModel = entry.sharedViewModel(navController)
            PartyArtistSelectRoute(
                onPopBackStack = navController::popBackStack,
                viewModel = viewModel,
                modifier = Modifier.padding(paddingValues),
            )
        }
    }
}

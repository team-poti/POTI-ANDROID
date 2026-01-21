package com.poti.android.presentation.party.create.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.poti.android.core.navigation.Route
import com.poti.android.presentation.party.create.PartyCreateRoute
import kotlinx.serialization.Serializable

sealed interface PartyCreateRoute : Route {
    @Serializable
    data object Create : PartyCreateRoute

    @Serializable
    data object ArtistSelect : PartyCreateRoute
}

fun NavController.navigateToPartyCreate() {
    navigate(PartyCreateRoute.Create)
}

fun NavController.navigateToPartyArtistSelect() {
    navigate(PartyCreateRoute.ArtistSelect)
}

fun NavGraphBuilder.partyCreateNavGraph(
    paddingValues: PaddingValues,
) {
    composable<PartyCreateRoute.Create> {
        PartyCreateRoute(
            onPopBackStack = {},
            onNavigateToSearch = {},
            modifier = Modifier.padding(paddingValues),
        )
    }
    composable<PartyCreateRoute.ArtistSelect> { }
}

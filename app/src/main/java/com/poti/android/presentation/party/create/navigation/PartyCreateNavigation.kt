package com.poti.android.presentation.party.create.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.poti.android.core.navigation.Route
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
    composable<PartyCreateRoute.Create> { }
    composable<PartyCreateRoute.ArtistSelect> { }
}

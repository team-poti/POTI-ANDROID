package com.poti.android.presentation.partycreate.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.poti.android.core.navigation.Route
import kotlinx.serialization.Serializable

sealed interface PartyCreateRoute : Route {
    @Serializable
    data object PartyCreate : PartyCreateRoute

    @Serializable
    data object PartyArtistSelect : PartyCreateRoute
}

fun NavGraphBuilder.partyCreateNavGraph(
    paddingValues: PaddingValues,
) {
    composable<PartyCreateRoute.PartyCreate> { }
    composable<PartyCreateRoute.PartyArtistSelect> { }
}

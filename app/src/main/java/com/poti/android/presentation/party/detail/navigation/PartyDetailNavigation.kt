package com.poti.android.presentation.party.detail.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.poti.android.core.navigation.Route
import com.poti.android.presentation.party.detail.PartyDetailRoute
import com.poti.android.presentation.party.detail.PartyJoinRoute
import kotlinx.serialization.Serializable

sealed interface PartyDetailRoute : Route {
    @Serializable
    data object PartyDetail : PartyDetailRoute

    @Serializable
    data object PartyJoin : PartyDetailRoute
}

fun NavGraphBuilder.partyDetailNavGraph(
    paddingValues: PaddingValues,
) {
    composable<PartyDetailRoute.PartyDetail> {
        PartyDetailRoute(
            modifier = Modifier.padding(paddingValues),
        )
    }
    composable<PartyDetailRoute.PartyJoin> {
        PartyJoinRoute(modifier = Modifier.padding(paddingValues))
    }
}

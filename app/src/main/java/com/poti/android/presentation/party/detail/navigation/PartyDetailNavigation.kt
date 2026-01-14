package com.poti.android.presentation.party.detail.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.poti.android.core.navigation.Route
import com.poti.android.presentation.party.detail.PartyDetailRoute
import com.poti.android.presentation.party.detail.PartyJoinRoute
import com.poti.android.presentation.user.profile.navigation.navigateToProfile
import kotlinx.serialization.Serializable

sealed interface PartyDetailRoute : Route {
    @Serializable
    data object Detail : PartyDetailRoute

    @Serializable
    data object Join : PartyDetailRoute
}

fun NavController.navigateToPartyDetail() {
    navigate(PartyDetailRoute.Detail)
}

fun NavController.navigateToPartyJoin() {
    navigate(PartyDetailRoute.Join)
}

fun NavGraphBuilder.partyDetailNavGraph(
    paddingValues: PaddingValues,
    navController: NavController,
) {
    composable<PartyDetailRoute.Detail> {
        PartyDetailRoute(
            modifier = Modifier.padding(paddingValues),
            onPopBackStack = navController::popBackStack,
            onNavigateToJoin = navController::navigateToPartyJoin,
            onNavigateToProfile = navController::navigateToProfile,
        )
    }
    composable<PartyDetailRoute.Join> {
        PartyJoinRoute(
            modifier = Modifier.padding(paddingValues),
        )
    }
}

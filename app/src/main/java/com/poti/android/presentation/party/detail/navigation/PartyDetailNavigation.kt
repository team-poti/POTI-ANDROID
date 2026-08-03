package com.poti.android.presentation.party.detail.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import androidx.navigation.navDeepLink
import com.poti.android.BuildConfig
import com.poti.android.core.common.extension.sharedViewModel
import com.poti.android.core.common.extension.slideComposable
import com.poti.android.core.navigation.Route
import com.poti.android.presentation.party.create.navigation.PartyCreateRoute
import com.poti.android.presentation.party.detail.PartyDetailRoute
import com.poti.android.presentation.party.detail.PartyJoinRoute
import com.poti.android.presentation.user.profile.navigation.navigateToProfile
import kotlinx.serialization.Serializable

@Serializable
data class PartyDetailGraph(val partyId: Long) : Route

sealed interface PartyDetailRoute : Route {
    @Serializable
    data object Detail : PartyDetailRoute

    @Serializable
    data object Join : PartyDetailRoute
}

fun NavController.navigateToPartyDetail(partyId: Long) {
    navigate(PartyDetailGraph(partyId))
}

fun NavController.navigateToPartyJoin() {
    navigate(PartyDetailRoute.Join)
}

fun NavController.reloadPartyDetail(partyId: Long) {
    navigate(PartyDetailGraph(partyId)) {
        popUpTo<PartyDetailGraph> {
            inclusive = true
        }
    }
}

fun NavController.navigateToPartyDetailFromCreate(partyId: Long) {
    navigate(PartyDetailGraph(partyId)) {
        popUpTo<PartyCreateRoute.Create> {
            inclusive = true
        }
    }
}

fun NavGraphBuilder.partyDetailNavGraph(
    paddingValues: PaddingValues,
    navController: NavController,
) {
    navigation<PartyDetailGraph>(
        startDestination = PartyDetailRoute.Detail,
        deepLinks = listOf(
            navDeepLink<PartyDetailGraph>(basePath = "${BuildConfig.DEEP_LINK_HOST}/pot"),
        ),
    ) {
        slideComposable<PartyDetailRoute.Detail> { entry ->
            PartyDetailRoute(
                onPopBackStack = navController::popBackStack,
                onNavigateToJoin = navController::navigateToPartyJoin,
                onNavigateToProfile = navController::navigateToProfile,
                onReload = navController::reloadPartyDetail,
                viewModel = entry.sharedViewModel(navController),
                modifier = Modifier.padding(paddingValues),
            )
        }
        slideComposable<PartyDetailRoute.Join> { entry ->
            PartyJoinRoute(
                onPopBackStack = navController::popBackStack,
                onReload = navController::reloadPartyDetail,
                viewModel = entry.sharedViewModel(navController),
                modifier = Modifier.padding(paddingValues),
            )
        }
    }
}

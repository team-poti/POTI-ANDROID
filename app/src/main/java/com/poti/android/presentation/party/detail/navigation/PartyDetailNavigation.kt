package com.poti.android.presentation.party.detail.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import androidx.navigation.navDeepLink
import com.poti.android.BuildConfig
import com.poti.android.core.analytics.AnalyticsValue
import com.poti.android.core.common.extension.sharedViewModel
import com.poti.android.core.common.extension.slideComposable
import com.poti.android.core.navigation.Route
import com.poti.android.presentation.auth.navigation.navigateToLogin
import com.poti.android.presentation.party.create.navigation.PartyCreateRoute
import com.poti.android.presentation.party.detail.PartyDetailRoute
import com.poti.android.presentation.party.detail.PartyJoinRoute
import com.poti.android.presentation.user.profile.navigation.navigateToProfile
import kotlinx.serialization.Serializable

private const val PARTY_DETAIL_DEEP_LINK_BASE_PATH = "${BuildConfig.DEEP_LINK_HOST}/pot"

@Serializable
data class PartyDetailGraph(
    val partyId: Long,
    val source: String = AnalyticsValue.DEEP_LINK,
) : Route

fun partyDetailDeepLink(partyId: Long): String =
    "$PARTY_DETAIL_DEEP_LINK_BASE_PATH/$partyId"

sealed interface PartyDetailRoute : Route {
    @Serializable
    data object Detail : PartyDetailRoute

    @Serializable
    data object Join : PartyDetailRoute
}

fun NavController.navigateToPartyDetail(partyId: Long) {
    navigateToPartyDetail(partyId, AnalyticsValue.GOODS)
}

fun NavController.navigateToPartyDetail(
    partyId: Long,
    source: String,
) {
    navigate(PartyDetailGraph(partyId, source))
}

fun NavController.navigateToPartyJoin() {
    navigate(PartyDetailRoute.Join)
}

fun NavController.reloadPartyDetail(
    partyId: Long,
    source: String,
) {
    navigate(PartyDetailGraph(partyId, source)) {
        popUpTo<PartyDetailGraph> {
            inclusive = true
        }
    }
}

fun NavController.navigateToPartyDetailFromCreate(partyId: Long) {
    navigate(PartyDetailGraph(partyId, AnalyticsValue.GOODS)) {
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
            navDeepLink<PartyDetailGraph>(basePath = PARTY_DETAIL_DEEP_LINK_BASE_PATH),
        ),
    ) {
        slideComposable<PartyDetailRoute.Detail> { entry ->
            PartyDetailRoute(
                onPopBackStack = navController::popBackStack,
                onNavigateToJoin = navController::navigateToPartyJoin,
                onNavigateToProfile = navController::navigateToProfile,
                onReload = navController::reloadPartyDetail,
                onNavigateToLogin = navController::navigateToLogin,
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

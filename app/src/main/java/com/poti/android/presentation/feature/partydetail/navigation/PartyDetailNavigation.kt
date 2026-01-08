package com.poti.android.presentation.feature.partydetail.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.poti.android.core.navigation.PartyDetailRoute
import com.poti.android.presentation.feature.partydetail.PartyDetailRoute
import com.poti.android.presentation.feature.partydetail.PartyJoinRoute

fun NavGraphBuilder.partyDetailNavGraph(
    paddingValues: PaddingValues,
) {
    composable<PartyDetailRoute.PartyDetail> {
        PartyDetailRoute(modifier = Modifier.padding(paddingValues))
    }
    composable<PartyDetailRoute.PartyJoin> {
        PartyJoinRoute(modifier = Modifier.padding(paddingValues))
    }
}

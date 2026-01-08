package com.poti.android.presentation.feature.partycreate.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.poti.android.core.navigation.PartyCreateRoute

fun NavGraphBuilder.partyCreateNavGraph(
    paddingValues: PaddingValues,
) {
    composable<PartyCreateRoute.PartyCreate> { }
    composable<PartyCreateRoute.PartyArtistSelect> { }
}

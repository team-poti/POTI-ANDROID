package com.poti.android.presentation.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.poti.android.presentation.auth.navigation.authNavGraph
import com.poti.android.presentation.history.navigation.historyNavGraph
import com.poti.android.presentation.onboarding.navigation.onboardingNavGraph
import com.poti.android.presentation.party.goodsfilter.navigation.navigateToGoodsCategory
import com.poti.android.presentation.party.partyNavGraph
import com.poti.android.presentation.user.mypage.navigation.myPageNavGraph
import com.poti.android.presentation.user.profile.navigation.profileNavGraph

@Composable
fun PotiNavHost(
    navigator: PotiNavigator,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navigator.navController,
        startDestination = navigator.startDestination,
        modifier = modifier.fillMaxSize(),
    ) {
        authNavGraph(
            paddingValues = paddingValues,
        )
        onboardingNavGraph(
            navController = navigator.navController,
            paddingValues = paddingValues,
        )
        partyNavGraph(
            paddingValues = paddingValues,
            onNavigateToGoodsCategory = navigator.navController::navigateToGoodsCategory,
        )
        historyNavGraph(
            paddingValues = paddingValues,
        )
        myPageNavGraph(
            paddingValues = paddingValues,
        )
        profileNavGraph(
            paddingValues = paddingValues,
        )
    }
}

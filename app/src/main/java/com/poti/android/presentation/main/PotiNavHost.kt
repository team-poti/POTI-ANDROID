package com.poti.android.presentation.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.poti.android.presentation.feature.auth.authNavGraph
import com.poti.android.presentation.feature.goods.navigation.goodsNavGraph
import com.poti.android.presentation.feature.home.navigation.homeNavGraph
import com.poti.android.presentation.feature.mypage.navigation.myPageNavGraph
import com.poti.android.presentation.feature.myparty.navigation.myPartyNavGraph
import com.poti.android.presentation.feature.onboarding.navigation.onboardingNavGraph
import com.poti.android.presentation.feature.partycreate.navigation.partyCreateNavGraph
import com.poti.android.presentation.feature.partydetail.navigation.partyDetailNavGraph
import com.poti.android.presentation.feature.profile.navigation.profileNavGraph

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
            paddingValues = paddingValues,
        )
        homeNavGraph(
            paddingValues = paddingValues,
        )
        goodsNavGraph(
            paddingValues = paddingValues,
        )
        myPartyNavGraph(
            paddingValues = paddingValues,
        )
        myPageNavGraph(
            paddingValues = paddingValues,
        )
        partyDetailNavGraph(
            paddingValues = paddingValues,
        )
        partyCreateNavGraph(
            paddingValues = paddingValues,
        )
        profileNavGraph(
            paddingValues = paddingValues,
        )
    }
}

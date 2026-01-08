package com.poti.android.presentation.feature.onboarding.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.poti.android.core.navigation.OnboardingRoute
import com.poti.android.presentation.feature.onboarding.OnboardingArtistRoute
import com.poti.android.presentation.feature.onboarding.OnboardingGuideRoute
import com.poti.android.presentation.feature.onboarding.OnboardingNicknameRoute

fun NavGraphBuilder.onboardingNavGraph(
    paddingValues: PaddingValues,
) {
    composable<OnboardingRoute.Guide> {
        OnboardingGuideRoute(modifier = Modifier.padding(paddingValues))
    }
    composable<OnboardingRoute.Nickname> {
        OnboardingNicknameRoute(modifier = Modifier.padding(paddingValues))
    }
    composable<OnboardingRoute.Artist> {
        OnboardingArtistRoute(modifier = Modifier.padding(paddingValues))
    }
}

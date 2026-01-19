package com.poti.android.presentation.user.mypage.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.poti.android.core.navigation.Route
import com.poti.android.presentation.onboarding.navigation.navigateToOnboardingArtist
import com.poti.android.presentation.user.mypage.MyPageRoute
import kotlinx.serialization.Serializable

sealed interface MyPageRoute : Route {
    @Serializable
    data object MyPage : MyPageRoute
}

fun NavController.navigateToMyPage() {
    navigate(MyPageRoute.MyPage)
}

fun NavGraphBuilder.myPageNavGraph(
    paddingValues: PaddingValues,
    navController: NavController,
) {
    composable<MyPageRoute.MyPage> {
        MyPageRoute(
            onNavigateToArtist = navController::navigateToOnboardingArtist,
            modifier = Modifier.padding(paddingValues),
        )
    }
}

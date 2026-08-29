package com.poti.android.presentation.user.mypage.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.poti.android.core.common.extension.slideComposable
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.core.navigation.Route
import com.poti.android.presentation.auth.navigation.navigateToLogin
import com.poti.android.presentation.history.navigation.navigateToHistoryList
import com.poti.android.presentation.user.favoriteartist.FavoriteArtistRoute
import com.poti.android.presentation.user.mypage.MyPageRoute
import com.poti.android.presentation.user.setting.navigation.navigateToSetting
import kotlinx.serialization.Serializable

sealed interface MyPageRoute : Route {
    @Serializable
    data object MyPage : MyPageRoute

    @Serializable
    data class FavoriteArtist(
        val favoriteArtistName: String? = null,
    ) : MyPageRoute
}

fun NavController.navigateToMyPage() {
    navigate(MyPageRoute.MyPage)
}

fun NavController.navigateToFavoriteArtist(favoriteArtistName: String?) {
    navigate(MyPageRoute.FavoriteArtist(favoriteArtistName))
}

fun NavGraphBuilder.myPageNavGraph(
    paddingValues: PaddingValues,
    navController: NavController,
) {
    composable<MyPageRoute.MyPage> {
        MyPageRoute(
            onNavigateToHistoryList = navController::navigateToHistoryList,
            onNavigateToSetting = navController::navigateToSetting,
            onNavigateToFavoriteArtist = navController::navigateToFavoriteArtist,
            onNavigateToLogin = navController::navigateToLogin,
            modifier = Modifier
                .fillMaxSize()
                .background(PotiTheme.colors.gray100)
                .padding(paddingValues),
        )
    }
    slideComposable<MyPageRoute.FavoriteArtist> {
        FavoriteArtistRoute(
            onPopBackStack = navController::popBackStack,
            modifier = Modifier.padding(paddingValues),
        )
    }
}

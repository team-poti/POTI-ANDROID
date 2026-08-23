package com.poti.android.presentation.user

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.poti.android.core.navigation.Route
import com.poti.android.presentation.user.mypage.navigation.MyPageRoute
import com.poti.android.presentation.user.mypage.navigation.myPageNavGraph
import com.poti.android.presentation.user.profile.navigation.profileNavGraph
import kotlinx.serialization.Serializable

@Serializable
object UserGraph : Route

fun NavGraphBuilder.userNavGraph(
    navController: NavController,
    paddingValues: PaddingValues,
) {
    navigation<UserGraph>(
        startDestination = MyPageRoute.MyPage,
    ) {
        myPageNavGraph(
            navController = navController,
            paddingValues = paddingValues,
        )
        profileNavGraph(
            paddingValues = paddingValues,
            onPopBackStack = navController::popBackStack,
        )
    }
}

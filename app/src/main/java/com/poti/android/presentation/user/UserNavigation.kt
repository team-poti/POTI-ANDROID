package com.poti.android.presentation.user

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.poti.android.core.navigation.Route
import com.poti.android.presentation.user.account.navigation.accountSettingNavGraph
import com.poti.android.presentation.user.address.navigation.addressManagementNavGraph
import com.poti.android.presentation.user.editprofile.navigation.editProfileNavGraph
import com.poti.android.presentation.user.mypage.navigation.MyPageRoute
import com.poti.android.presentation.user.mypage.navigation.myPageNavGraph
import com.poti.android.presentation.user.profile.navigation.profileNavGraph
import com.poti.android.presentation.user.setting.navigation.settingNavGraph
import com.poti.android.presentation.user.withdrawal.navigation.withdrawalNavGraph
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
        settingNavGraph(
            navController = navController,
            paddingValues = paddingValues,
        )
        accountSettingNavGraph(
            navController = navController,
            paddingValues = paddingValues,
        )
        editProfileNavGraph(
            navController = navController,
            paddingValues = paddingValues,
        )
        addressManagementNavGraph(
            navController = navController,
            paddingValues = paddingValues,
        )
        withdrawalNavGraph(
            navController = navController,
            paddingValues = paddingValues,
        )
    }
}

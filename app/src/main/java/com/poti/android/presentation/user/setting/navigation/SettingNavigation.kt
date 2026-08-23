package com.poti.android.presentation.user.setting.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.poti.android.core.common.extension.slideComposable
import com.poti.android.core.navigation.Route
import com.poti.android.presentation.user.account.navigation.navigateToAccountSetting
import com.poti.android.presentation.user.address.navigation.navigateToAddressManagement
import com.poti.android.presentation.user.editprofile.navigation.navigateToEditProfile
import com.poti.android.presentation.user.setting.SettingRoute
import kotlinx.serialization.Serializable

sealed interface SettingRoute : Route {
    @Serializable
    data object Setting : SettingRoute
}

fun NavController.navigateToSetting() {
    navigate(SettingRoute.Setting)
}

fun NavGraphBuilder.settingNavGraph(
    navController: NavController,
    paddingValues: PaddingValues,
) {
    slideComposable<SettingRoute.Setting> {
        SettingRoute(
            onPopBackStack = navController::popBackStack,
            onNavigateToAccount = navController::navigateToAccountSetting,
            onNavigateToProfileManagement = navController::navigateToEditProfile,
            onNavigateToAddressManagement = navController::navigateToAddressManagement,
            onNavigateToAlarmSetting = {},
            onNavigateToPersonalInfoPrivacy = {},
            modifier = Modifier.padding(paddingValues),
        )
    }
}

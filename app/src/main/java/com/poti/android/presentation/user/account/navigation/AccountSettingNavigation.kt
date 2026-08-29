package com.poti.android.presentation.user.account.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.poti.android.core.common.extension.slideComposable
import com.poti.android.core.navigation.Route
import com.poti.android.presentation.user.account.AccountSettingRoute
import com.poti.android.presentation.user.withdrawal.navigation.navigateToWithdrawal
import kotlinx.serialization.Serializable

sealed interface AccountSettingRoute : Route {
    @Serializable
    data object AccountSetting : AccountSettingRoute
}

fun NavController.navigateToAccountSetting() {
    navigate(AccountSettingRoute.AccountSetting)
}

fun NavGraphBuilder.accountSettingNavGraph(
    navController: NavController,
    paddingValues: PaddingValues,
) {
    slideComposable<AccountSettingRoute.AccountSetting> {
        AccountSettingRoute(
            onPopBackStack = navController::popBackStack,
            onNavigateToWithdrawal = navController::navigateToWithdrawal,
            modifier = Modifier.padding(paddingValues),
        )
    }
}

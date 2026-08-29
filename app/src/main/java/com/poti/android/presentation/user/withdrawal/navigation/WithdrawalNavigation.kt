package com.poti.android.presentation.user.withdrawal.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.poti.android.core.common.extension.slideComposable
import com.poti.android.core.navigation.Route
import com.poti.android.presentation.user.withdrawal.WithdrawalRoute
import kotlinx.serialization.Serializable

sealed interface WithdrawalRoute : Route {
    @Serializable
    data object Withdrawal : WithdrawalRoute
}

fun NavController.navigateToWithdrawal() {
    navigate(WithdrawalRoute.Withdrawal)
}

fun NavGraphBuilder.withdrawalNavGraph(
    navController: NavController,
    paddingValues: PaddingValues,
) {
    slideComposable<WithdrawalRoute.Withdrawal> {
        WithdrawalRoute(
            onPopBackStack = navController::popBackStack,
            modifier = Modifier.padding(paddingValues),
        )
    }
}

package com.poti.android.presentation.alarm.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.poti.android.core.common.extension.slideComposable
import com.poti.android.core.navigation.Route
import com.poti.android.presentation.alarm.list.AlarmListRoute
import com.poti.android.presentation.alarm.setting.AlarmSettingRoute
import kotlinx.serialization.Serializable

sealed interface AlarmRoute : Route {
    @Serializable
    data object AlarmList : AlarmRoute

    @Serializable
    data object AlarmSetting : AlarmRoute
}

fun NavController.navigateToAlarmList() {
    navigate(AlarmRoute.AlarmList)
}

fun NavController.navigateToAlarmSetting() {
    navigate(AlarmRoute.AlarmSetting)
}

fun NavGraphBuilder.alarmNavGraph(
    navController: NavController,
    paddingValues: PaddingValues,
) {
    slideComposable<AlarmRoute.AlarmList> {
        AlarmListRoute(
            onPopBackStack = navController::popBackStack,
            navigateToSetting = navController::navigateToAlarmSetting,
            modifier = Modifier
                .consumeWindowInsets(paddingValues)
                .padding(paddingValues),
        )
    }

    slideComposable<AlarmRoute.AlarmSetting> {
        AlarmSettingRoute(
            onPopBackStack = navController::popBackStack,
            modifier = Modifier.padding(paddingValues),
        )
    }
}

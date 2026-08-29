package com.poti.android.presentation.user.address.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.poti.android.core.common.extension.slideComposable
import com.poti.android.core.navigation.Route
import com.poti.android.presentation.user.address.AddressManagementRoute
import kotlinx.serialization.Serializable

sealed interface AddressManagementRoute : Route {
    @Serializable
    data object AddressManagement : AddressManagementRoute
}

fun NavController.navigateToAddressManagement() {
    navigate(AddressManagementRoute.AddressManagement)
}

fun NavGraphBuilder.addressManagementNavGraph(
    navController: NavController,
    paddingValues: PaddingValues,
) {
    slideComposable<AddressManagementRoute.AddressManagement> {
        AddressManagementRoute(
            onPopBackStack = navController::popBackStack,
            modifier = Modifier.padding(paddingValues),
        )
    }
}

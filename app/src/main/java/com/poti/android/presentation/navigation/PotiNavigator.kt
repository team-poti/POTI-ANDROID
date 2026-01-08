package com.poti.android.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.poti.android.core.navigation.AuthRoute

class PotiNavigator(
    val navController: NavHostController,
) {
    val startDestination = AuthRoute.Login
}

@Composable
fun rememberPotiNavigator(
    navController: NavHostController = rememberNavController(),
): PotiNavigator = remember(navController) {
    PotiNavigator(navController)
}

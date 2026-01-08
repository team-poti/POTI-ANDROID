package com.poti.android.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

class PotiNavigator(
    val navController: NavHostController,
) {
    val startDestination = Route.Splash
}

@Composable
fun rememberPotiNavigator(
    navController: NavHostController = rememberNavController(),
): PotiNavigator = remember(navController) {
    PotiNavigator(navController)
}

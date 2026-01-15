package com.poti.android.presentation.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.poti.android.presentation.onboarding.navigation.OnboardingRoute

class PotiNavigator(
    val navController: NavHostController,
) {
    private val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val startDestination = OnboardingRoute.Guide

    val currentTab: MainTab?
        @Composable get() = MainTab.entries.find { tab ->
            currentDestination?.route == tab.route::class.qualifiedName
        }

    fun navigate(tab: MainTab) {
        val navOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        navController.navigate(tab.route, navOptions)
    }

    @Composable
    fun shouldShowBottomBar() = currentTab != null
}

@Composable
fun rememberPotiNavigator(
    navController: NavHostController = rememberNavController(),
): PotiNavigator = remember(navController) {
    PotiNavigator(navController)
}

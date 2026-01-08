package com.poti.android.presentation.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.poti.android.core.navigation.HomeRoute
import com.poti.android.core.navigation.MyPageRoute
import com.poti.android.core.navigation.MyPartyRoute

class PotiNavigator(
    val navController: NavHostController,
) {
    private val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val startDestination = HomeRoute.Home

    val currentTab: MainTab?
        @Composable get() = MainTab.entries.find { tab ->
            when (tab.route) {
                else -> currentDestination?.route == tab.route::class.qualifiedName
            }
        }

    fun navigate(tab: MainTab) {
        val navOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        val route = when (tab) {
            MainTab.HOME -> HomeRoute.Home
            MainTab.MY_PARTY -> MyPartyRoute.MyPartyList
            MainTab.MY_PAGE -> MyPageRoute.MyPage
        }

        navController.navigate(route, navOptions)
    }

    @Composable
    fun shouldShowBottomBar() = MainTab.contains(currentDestination)
}

@Composable
fun rememberPotiNavigator(
    navController: NavHostController = rememberNavController(),
): PotiNavigator = remember(navController) {
    PotiNavigator(navController)
}

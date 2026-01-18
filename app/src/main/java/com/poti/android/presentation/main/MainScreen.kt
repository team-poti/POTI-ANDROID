package com.poti.android.presentation.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.poti.android.core.navigation.Route

@Composable
fun MainScreen(
    startDestination: Route,
    navigator: MainNavigator = rememberPotiNavigator(),
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            MainBottomBar(
                visible = navigator.shouldShowBottomBar(),
                currentTab = navigator.currentTab,
                onTabSelected = { navigator.navigate(it) },
                modifier = Modifier.navigationBarsPadding(),
            )
        },
    ) { innerPadding ->
        MainNavHost(
            navigator = navigator,
            startDestination = startDestination,
            paddingValues = innerPadding,
        )
    }
}

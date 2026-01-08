package com.poti.android.presentation.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.poti.android.presentation.navigation.PotiNavHost
import com.poti.android.presentation.navigation.PotiNavigator
import com.poti.android.presentation.navigation.rememberPotiNavigator

@Composable
fun MainScreen(
    navigator: PotiNavigator = rememberPotiNavigator(),
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
        },
    ) { innerPadding ->
        PotiNavHost(
            navigator = navigator,
            paddingValues = innerPadding,
        )
    }
}

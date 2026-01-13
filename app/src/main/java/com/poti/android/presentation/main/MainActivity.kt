package com.poti.android.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.poti.android.core.designsystem.theme.PotiTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var keepSplashScreen = true
        lifecycleScope.launch {
            delay(2000)
            keepSplashScreen = false
        }
        splashScreen.setKeepOnScreenCondition {
            keepSplashScreen
        }

        enableEdgeToEdge()
        setContent {
            val potiNavigator: PotiNavigator = rememberPotiNavigator()
            PotiTheme {
                MainScreen(navigator = potiNavigator)
            }
        }
    }
}

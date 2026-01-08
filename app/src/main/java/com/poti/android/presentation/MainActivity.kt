package com.poti.android.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.presentation.main.MainScreen
import com.poti.android.presentation.main.PotiNavigator
import com.poti.android.presentation.main.rememberPotiNavigator

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val potiNavigator: PotiNavigator = rememberPotiNavigator()
            PotiTheme {
                MainScreen(navigator = potiNavigator)
            }
        }
    }
}

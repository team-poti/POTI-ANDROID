package com.poti.android.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.presentation.main.MainScreen
import com.poti.android.presentation.navigation.PotiNavigator
import com.poti.android.presentation.navigation.rememberPotiNavigator

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
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

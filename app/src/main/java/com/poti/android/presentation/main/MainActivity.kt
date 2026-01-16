package com.poti.android.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.poti.android.core.designsystem.theme.PotiTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var isSplashTimeFinished = false

        lifecycleScope.launch {
            delay(2000)
            isSplashTimeFinished = true
        }

        splashScreen.setKeepOnScreenCondition {
            viewModel.startDestination.value == null || !isSplashTimeFinished
        }

        enableEdgeToEdge()
        setContent {
            val mainNavigator: MainNavigator = rememberPotiNavigator()
            val startDestination by viewModel.startDestination.collectAsStateWithLifecycle()

            PotiTheme {
                startDestination?.let { destination ->
                    MainScreen(
                        startDestination = destination,
                        navigator = mainNavigator,
                    )
                }
            }
        }
    }
}

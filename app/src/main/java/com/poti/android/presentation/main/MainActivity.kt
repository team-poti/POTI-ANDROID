package com.poti.android.presentation.main

import android.content.Intent
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
import com.poti.android.domain.manager.AuthSessionManager
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var authSessionManager: AuthSessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition {
            viewModel.startDestination.value == null
        }

        lifecycleScope.launch {
            authSessionManager.logoutEvent.collect {
                handleLogoutNavigation()
            }
        }

        enableEdgeToEdge()

        setContent {
            val mainNavigator: MainNavigator = rememberPotiNavigator()
            val targetDestination by viewModel.startDestination.collectAsStateWithLifecycle()

            PotiTheme {
                targetDestination?.let { destination ->
                    MainScreen(
                        targetDestination = destination,
                        navigator = mainNavigator,
                    )
                }
            }
        }
    }

    private fun handleLogoutNavigation() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}

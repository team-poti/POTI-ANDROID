package com.poti.android.presentation.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.poti.android.core.auth.SocialLoginLauncher
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.domain.manager.AuthSessionManager
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var authSessionManager: AuthSessionManager

    @Inject
    lateinit var socialLoginLauncher: SocialLoginLauncher

    private var pendingDeepLink by mutableStateOf<Uri?>(null)

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

        pendingDeepLink = intent?.data

        setContent {
            val mainNavigator: MainNavigator = rememberPotiNavigator()
            val targetDestination by viewModel.startDestination.collectAsStateWithLifecycle()

            LaunchedEffect(targetDestination, pendingDeepLink) {
                val deepLink = pendingDeepLink ?: return@LaunchedEffect
                if (targetDestination == null) return@LaunchedEffect

                runCatching {
                    mainNavigator.navController.navigate(deepLink)
                }.onFailure {
                    Timber.e(it, "Failed to navigate to deep link: $deepLink")
                }
                pendingDeepLink = null
            }

            PotiTheme {
                targetDestination?.let { destination ->
                    MainScreen(
                        targetDestination = destination,
                        navigator = mainNavigator,
                        socialLoginLauncher = socialLoginLauncher,
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        pendingDeepLink = intent.data
    }

    private fun handleLogoutNavigation() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}

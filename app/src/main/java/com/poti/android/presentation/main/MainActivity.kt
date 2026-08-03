package com.poti.android.presentation.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import com.poti.android.core.auth.SocialLoginLauncher
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.domain.manager.AuthSessionManager
import com.poti.android.presentation.party.PartyGraph
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    private var navController: NavHostController? = null

    @Inject
    lateinit var authSessionManager: AuthSessionManager

    @Inject
    lateinit var socialLoginLauncher: SocialLoginLauncher

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
            val coldStartDeepLink = remember { intent.data }

            navController = mainNavigator.navController

            PotiTheme {
                targetDestination?.let { destination ->
                    MainScreen(
                        targetDestination = destination,
                        navigator = mainNavigator,
                        socialLoginLauncher = socialLoginLauncher,
                        onSplashFinished = {
                            if (destination == PartyGraph) {
                                coldStartDeepLink?.let { mainNavigator.navController.navigateToDeepLink(it) }
                            }
                        },
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        intent.data?.let { uri -> navController?.navigateToDeepLink(uri) }
    }

    private fun NavHostController.navigateToDeepLink(uri: Uri) {
        runCatching { navigate(uri) }
            .onFailure { Timber.w(it, "Unhandled deep link: $uri") }
    }

    private fun handleLogoutNavigation() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}

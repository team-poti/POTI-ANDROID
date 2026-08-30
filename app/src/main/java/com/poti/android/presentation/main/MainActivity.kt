package com.poti.android.presentation.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import com.poti.android.core.auth.SocialLoginLauncher
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.core.share.KakaoShareManager
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
    private var pendingDeepLink: Uri? by mutableStateOf(null)

    @Inject
    lateinit var authSessionManager: AuthSessionManager

    @Inject
    lateinit var socialLoginLauncher: SocialLoginLauncher

    private val requestNotificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

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

        requestNotificationPermission()

        pendingDeepLink = intent?.resolveDeepLink()
        intent?.data = null

        setContent {
            val mainNavigator: MainNavigator = rememberPotiNavigator()
            val targetDestination by viewModel.startDestination.collectAsStateWithLifecycle()

            navController = mainNavigator.navController

            val onAuthCompleted = {
                mainNavigator.navigateToHome()
                consumeDeepLink(mainNavigator.navController)
            }

            PotiTheme {
                targetDestination?.let { destination ->
                    MainScreen(
                        targetDestination = destination,
                        navigator = mainNavigator,
                        socialLoginLauncher = socialLoginLauncher,
                        onSplashFinished = {
                            if (destination == PartyGraph) {
                                consumeDeepLink(mainNavigator.navController)
                            }
                        },
                        onLoginSuccess = onAuthCompleted,
                        onOnboardingFinished = onAuthCompleted,
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)

        val uri = intent.resolveDeepLink() ?: return
        intent.data = null

        if (viewModel.startDestination.value == PartyGraph) {
            navController?.navigateToDeepLink(uri)
        } else {
            pendingDeepLink = uri
        }
    }

    private fun Intent.resolveDeepLink(): Uri? {
        val uri = data ?: return null
        if (uri.host != KakaoShareManager.LINK_HOST) return uri

        return uri.getQueryParameter(KakaoShareManager.PARAM_DEEP_LINK)?.toUri()
    }

    private fun consumeDeepLink(navController: NavHostController) {
        val uri = pendingDeepLink ?: return
        pendingDeepLink = null
        navController.navigateToDeepLink(uri)
    }

    private fun NavHostController.navigateToDeepLink(uri: Uri) {
        runCatching { navigate(uri) }
            .onFailure { Timber.w(it, "Unhandled deep link: $uri") }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return

        val isGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS,
        ) == PackageManager.PERMISSION_GRANTED

        if (!isGranted) {
            requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
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

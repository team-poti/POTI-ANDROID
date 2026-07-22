package com.poti.android.presentation.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poti.android.R
import com.poti.android.core.auth.SocialLoginLauncher
import com.poti.android.core.common.state.ApiState
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.domain.model.auth.SocialType
import com.poti.android.presentation.auth.component.LoginButton
import com.poti.android.presentation.auth.model.LoginEffect
import com.poti.android.presentation.auth.model.LoginIntent

@Composable
fun LoginRoute(
    onNavigateToOnboarding: () -> Unit,
    onNavigateToHome: () -> Unit,
    socialLoginLauncher: SocialLoginLauncher,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is LoginEffect.NavigateToOnboarding -> onNavigateToOnboarding()
                is LoginEffect.NavigateToHome -> onNavigateToHome()
                LoginEffect.LaunchKakaoLogin -> {
                    val socialType = SocialType.KAKAO
                    val result = socialLoginLauncher.login(context, socialType)
                    viewModel.processIntent(
                        LoginIntent.OnSocialLoginResult(
                            socialType = socialType,
                            result = result,
                        ),
                    )
                }
            }
        }
    }

    LoginScreen(
        modifier = modifier,
        isLoginInProgress = uiState.loginState is ApiState.Loading,
        onKakaoClick = { viewModel.processIntent(LoginIntent.OnKakaoLoginClick) },
        onGoogleClick = { viewModel.processIntent(LoginIntent.OnGoogleLoginClick) },
    )
}

@Composable
private fun LoginScreen(
    modifier: Modifier = Modifier,
    isLoginInProgress: Boolean,
    onKakaoClick: () -> Unit,
    onGoogleClick: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(267.dp))

        Image(
            imageVector = ImageVector.vectorResource(R.drawable.ic_poti_logo),
            contentDescription = null,
            modifier = Modifier.width(140.dp),
            contentScale = ContentScale.Crop,
        )

        Spacer(modifier = Modifier.height(17.dp))

        Text(
            text = stringResource(R.string.login_poti_description),
            style = PotiTheme.typography.title18sb,
            color = PotiTheme.colors.poti600,
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            horizontalArrangement = Arrangement.spacedBy(28.dp),
        ) {
            LoginButton(
                iconResId = R.drawable.ic_kakao,
                background = Color(0xFFFEE500),
                enabled = !isLoginInProgress,
                onClick = onKakaoClick,
            )

            LoginButton(
                iconResId = R.drawable.ic_google,
                background = Color.White,
                enabled = !isLoginInProgress,
                onClick = onGoogleClick,
            )
        }

        Spacer(modifier = Modifier.height(162.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    PotiTheme {
        LoginScreen(
            isLoginInProgress = false,
            onKakaoClick = {},
            onGoogleClick = {},
        )
    }
}

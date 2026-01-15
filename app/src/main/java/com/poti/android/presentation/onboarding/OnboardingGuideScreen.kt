package com.poti.android.presentation.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.poti.android.R
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.presentation.onboarding.component.OnboardingScaffold

@Composable
fun OnboardingGuideRoute(modifier: Modifier = Modifier) {
    OnboardingGuideScreen(
        onNavigateToLogin = {},
        onNavigateToOnboardingNickname = {},
        modifier = modifier,
    )
}

@Composable
private fun OnboardingGuideScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToOnboardingNickname: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OnboardingScaffold(
        currentStep = 1,
        title = stringResource(R.string.onboarding_Guide_label),
        onBackClick = onNavigateToLogin,
        onNextClick = onNavigateToOnboardingNickname,
        modifier = modifier,
        content = {},
    )
}

@Preview
@Composable
private fun OnboardingGuideScreenPreview() {
    PotiTheme {
        OnboardingGuideScreen(
            onNavigateToLogin = {},
            onNavigateToOnboardingNickname = {},
        )
    }
}

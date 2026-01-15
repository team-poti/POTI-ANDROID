package com.poti.android.presentation.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.poti.android.R
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.presentation.onboarding.component.OnboardingScaffold

@Composable
fun OnboardingGuideRoute(
    onPopBackStack: () -> Unit,
    onNavigateToOnboardingNickname: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OnboardingGuideScreen(
        onPopBackStack = onPopBackStack,
        onNavigateToOnboardingNickname = onNavigateToOnboardingNickname,
        modifier = modifier,
    )
}

@Composable
private fun OnboardingGuideScreen(
    onPopBackStack: () -> Unit,
    onNavigateToOnboardingNickname: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OnboardingScaffold(
        currentStep = 1,
        title = stringResource(R.string.onboarding_guide_label),
        onBackClick = onPopBackStack,
        onNextClick = onNavigateToOnboardingNickname,
        modifier = modifier,
        content = {}, // TODO: [지현] 디자인 나오면 수정
    )
}

@Preview
@Composable
private fun OnboardingGuideScreenPreview() {
    PotiTheme {
        OnboardingGuideScreen(
            onPopBackStack = {},
            onNavigateToOnboardingNickname = {},
        )
    }
}

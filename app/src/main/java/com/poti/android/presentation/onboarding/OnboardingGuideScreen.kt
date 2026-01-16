package com.poti.android.presentation.onboarding

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.common.util.HandleSideEffects
import com.poti.android.core.common.util.screenWidthDp
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.presentation.onboarding.component.OnboardingScaffold
import com.poti.android.presentation.onboarding.model.OnboardingUiEffect
import com.poti.android.presentation.onboarding.model.OnboardingUiIntent

@Composable
fun OnboardingGuideRoute(
    onPopBackStack: () -> Unit,
    onNavigateToOnboardingNickname: () -> Unit,
    viewModel: OnboardingViewModel,
    modifier: Modifier = Modifier,
) {
    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            OnboardingUiEffect.NavigateToBack -> onPopBackStack()
            OnboardingUiEffect.NavigateToNickname -> onNavigateToOnboardingNickname()
            else -> {}
        }
    }

    OnboardingGuideScreen(
        onPopBackStack = { viewModel.processIntent(OnboardingUiIntent.OnBackClick) },
        onNavigateToOnboardingNickname = { viewModel.processIntent(OnboardingUiIntent.OnGuideNextClick) },
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
        onBackClick = onPopBackStack,
        onNextClick = onNavigateToOnboardingNickname,
        modifier = modifier,
        content = {
            Text(
                text = stringResource(R.string.onboarding_guide_label),
                style = PotiTheme.typography.title18sb,
                color = PotiTheme.colors.black,
                modifier = Modifier.padding(horizontal = screenWidthDp(20.dp), vertical = 24.dp),
            )
        },
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

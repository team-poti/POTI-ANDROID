package com.poti.android.presentation.onboarding

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poti.android.R
import com.poti.android.core.common.util.HandleSideEffects
import com.poti.android.core.common.util.screenWidthDp
import com.poti.android.core.designsystem.component.field.PotiCountField
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.presentation.onboarding.component.OnboardingScaffold
import com.poti.android.presentation.onboarding.model.OnboardingUiEffect
import com.poti.android.presentation.onboarding.model.OnboardingUiIntent
import com.poti.android.presentation.onboarding.model.OnboardingUiState

@Composable
fun OnboardingNicknameRoute(
    onPopBackStack: () -> Unit,
    onNavigateToOnboardingArtist: () -> Unit,
    viewModel: OnboardingViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            OnboardingUiEffect.NavigateToBack -> onPopBackStack()
            OnboardingUiEffect.NavigateToArtist -> onNavigateToOnboardingArtist()
            else -> {}
        }
    }

    OnboardingNicknameScreen(
        uiState = uiState,
        onNicknameChange = { viewModel.processIntent(OnboardingUiIntent.OnNicknameChange(it)) },
        onPopBackStack = { viewModel.processIntent(OnboardingUiIntent.OnBackClick) },
        onNavigateToOnboardingArtist = { viewModel.processIntent(OnboardingUiIntent.OnNicknameNextClick) },
        modifier = modifier,
    )
}

@Composable
private fun OnboardingNicknameScreen(
    uiState: OnboardingUiState,
    onNicknameChange: (String) -> Unit,
    onPopBackStack: () -> Unit,
    onNavigateToOnboardingArtist: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OnboardingScaffold(
        currentStep = 2,
        onBackClick = onPopBackStack,
        onNextClick = onNavigateToOnboardingArtist,
        modifier = modifier,
        enabled = uiState.isNicknameValid,
    ) {
        Text(
            text = stringResource(R.string.onboarding_nickname_label),
            style = PotiTheme.typography.title18sb,
            color = PotiTheme.colors.black,
            modifier = Modifier.padding(horizontal = screenWidthDp(20.dp), vertical = 24.dp),
        )

        PotiCountField(
            value = uiState.nickname,
            onValueChanged = onNicknameChange,
            placeholder = stringResource(R.string.onboarding_nickname_field_placeholder),
            maxLength = 10,
            modifier = Modifier.padding(horizontal = screenWidthDp(16.dp)),
            error = uiState.nicknameError?.asString() ?: "",
        )
    }
}

@Preview
@Composable
private fun OnboardingNicknameScreenPreview() {
    PotiTheme {
        OnboardingNicknameScreen(
            uiState = OnboardingUiState(),
            onNicknameChange = {},
            onPopBackStack = {},
            onNavigateToOnboardingArtist = {},
        )
    }
}

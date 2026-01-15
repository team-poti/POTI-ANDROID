package com.poti.android.presentation.onboarding

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
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
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            OnboardingUiEffect.NavigateToBack -> onPopBackStack()
            OnboardingUiEffect.NavigateToHome -> onNavigateToOnboardingArtist()
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
        title = stringResource(R.string.onboarding_nickname_label),
        onBackClick = onPopBackStack,
        onNextClick = onNavigateToOnboardingArtist,
        modifier = modifier,
    ) {
        PotiCountField(
            value = uiState.nickname,
            onValueChanged = onNicknameChange,
            placeholder = stringResource(R.string.onboarding_nickname_field_placeholder),
            maxLength = 10,
            modifier = Modifier.padding(horizontal = screenWidthDp(16.dp)),
            error = "", // TODO: [지현] 에러 처리
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

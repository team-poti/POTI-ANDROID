package com.poti.android.presentation.onboarding

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.common.util.screenWidthDp
import com.poti.android.core.designsystem.component.field.PotiCountField
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.presentation.onboarding.component.OnboardingScaffold

@Composable
fun OnboardingNicknameRoute(
    onPopBackStack: () -> Unit,
    onNavigateToOnboardingArtist: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OnboardingNicknameScreen(
        onPopBackStack = onPopBackStack,
        onNavigateToOnboardingArtist = onNavigateToOnboardingArtist,
        modifier = modifier,
    )
}

@Composable
private fun OnboardingNicknameScreen(
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
            value = "", // TODO: [지현] value 연결
            onValueChanged = {}, // TODO: [지현] 람다 연결
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
            onPopBackStack = {},
            onNavigateToOnboardingArtist = {},
        )
    }
}

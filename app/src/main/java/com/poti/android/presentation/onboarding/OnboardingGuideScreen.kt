package com.poti.android.presentation.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.common.util.screenWidthDp
import com.poti.android.core.designsystem.component.display.PotiStepper
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage
import com.poti.android.core.designsystem.theme.PotiTheme

@Composable
fun OnboardingGuideRoute(modifier: Modifier = Modifier) {
    OnboardingGuideScreen(
        onNavigateToLogin = {},
        modifier = modifier,
    )
}

@Composable
private fun OnboardingGuideScreen(
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        PotiHeaderPage(onNavigationClick = onNavigateToLogin)

        PotiStepper(
            step = 1,
            modifier = Modifier.padding(vertical = 4.dp),
        )

        Text(
            text = stringResource(R.string.onboarding_Guide_label),
            style = PotiTheme.typography.title18sb,
            color = PotiTheme.colors.black,
            modifier = Modifier.padding(horizontal = screenWidthDp(20.dp), vertical = 24.dp),
        )
    }
}

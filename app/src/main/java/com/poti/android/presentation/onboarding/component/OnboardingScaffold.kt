package com.poti.android.presentation.onboarding.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.designsystem.component.display.PotiStepper
import com.poti.android.core.designsystem.component.navigation.PotiBottomButton
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage

@Composable
fun OnboardingScaffold(
    currentStep: Int,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
    isButtonVisible: Boolean = true,
    onSkip: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            PotiHeaderPage(onNavigationClick = onBackClick)
        },
        bottomBar = {
            if (isButtonVisible) {
                if (onSkip == null) {
                    PotiBottomButton(
                        text = stringResource(R.string.action_button_next),
                        onClick = onNextClick,
                    )
                } else {
                    PotiBottomButton(
                        text = stringResource(R.string.action_button_start),
                        onClick = onNextClick,
                        subText = stringResource(R.string.action_button_skip),
                        onSubClick = onSkip,
                    )
                }
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            PotiStepper(
                step = currentStep,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
            )

            content()
        }
    }
}

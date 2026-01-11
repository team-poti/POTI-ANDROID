package com.poti.android.core.designsystem.component.display

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.core.designsystem.theme.PotiTheme.colors

@Composable
fun PotiStepper(
    step: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        StepDot(isSelected = step == 1, modifier = Modifier.weight(1f))
        StepDot(isSelected = step == 2, modifier = Modifier.weight(1f))
        StepDot(isSelected = step == 3, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun StepDot(isSelected: Boolean, modifier: Modifier = Modifier) {
    val backgroundColor = if (isSelected) colors.poti600 else colors.gray100
    Box(
        modifier = modifier
            .height(8.dp)
            .clip(RoundedCornerShape(99.dp))
            .background(backgroundColor)
    )
}

@Preview(showBackground = true)
@Composable
private fun PotiStepperPreview() {
    PotiTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PotiStepper(step = 1)
            PotiStepper(step = 2)
            PotiStepper(step = 3)
        }
    }
}

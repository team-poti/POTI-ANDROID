package com.poti.android.core.designsystem.component.display

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.core.designsystem.theme.PotiTheme.colors
import com.poti.android.core.designsystem.theme.PotiTheme.typography
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun PotiListRadio(
    options: ImmutableList<String>,
    selectedOptionIndex: Int,
    onClick: (index: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        options.forEachIndexed { index, option ->
            PotiListRadioItem(
                text = option,
                selected = index == selectedOptionIndex,
                onClick = { onClick(index) },
            )
            if (index != options.lastIndex) {
                PotiDivider(styleType = PotiDividerStyle.SMALL)
            }
        }
    }
}

@Composable
fun PotiListRadioItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                interactionSource = null,
                indication = null,
                onClick = onClick,
            )
            .heightIn(48.dp)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = text,
            style = typography.body16m,
            color = colors.black,
        )
        if (selected) {
            PotiCheckBox(selected = true)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PotiListRadioPreview() {
    var selected by remember { mutableStateOf(1) }

    PotiTheme {
        PotiListRadio(
            options = persistentListOf("최신순", "인기순", "마감임박순", "평점순"),
            selectedOptionIndex = selected,
            onClick = { index -> selected = index },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PotiListRadioItemPreview() {
    var selectedItem1 by remember { mutableStateOf(true) }
    var selectedItem2 by remember { mutableStateOf(false) }

    PotiTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            PotiListRadioItem(
                text = "최신순",
                selected = selectedItem1,
                onClick = { selectedItem1 = !selectedItem1 },
            )
            PotiListRadioItem(
                text = "인기순",
                selected = selectedItem2,
                onClick = { selectedItem2 = !selectedItem2 },
            )
        }
    }
}

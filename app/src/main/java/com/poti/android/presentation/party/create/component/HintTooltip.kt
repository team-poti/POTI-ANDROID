package com.poti.android.presentation.party.create.component

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import com.poti.android.R
import com.poti.android.core.common.util.screenHeightDp
import com.poti.android.core.designsystem.component.button.PotiInlineButton
import com.poti.android.core.designsystem.theme.PotiTheme

private const val VISUAL_GAP_PX = 1

@Composable
fun HintToolTip(
    modifier: Modifier = Modifier,
    @StringRes text: Int = R.string.create_msg_hint,
    yOffset: Dp = 9.dp,
) {
    val density = LocalDensity.current

    val popupPositionProvider = remember(yOffset, density) {
        val offsetYPx = with(density) { yOffset.roundToPx() } + VISUAL_GAP_PX

        object : PopupPositionProvider {
            override fun calculatePosition(
                anchorBounds: IntRect,
                windowSize: IntSize,
                layoutDirection: LayoutDirection,
                popupContentSize: IntSize,
            ): IntOffset {
                return IntOffset(
                    x = windowSize.width / 2,
                    y = anchorBounds.top - popupContentSize.height - offsetYPx,
                )
            }
        }
    }

    val popupProperties = remember {
        PopupProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
        )
    }

    Popup(
        popupPositionProvider = popupPositionProvider,
        properties = popupProperties,
    ) {
        Box(
            modifier = modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopCenter,
        ) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.img_create_hint),
                contentDescription = null,
            )

            Text(
                text = stringResource(text),
                modifier = Modifier
                    .padding(top = screenHeightDp(11.dp)),
                color = PotiTheme.colors.poti600,
                style = PotiTheme.typography.body14sb,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun HintToolTipPreview() {
    var showHint by remember { mutableStateOf(false) }

    PotiTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 100.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PotiInlineButton(
                text = "힌트 보여주기",
                onClick = { showHint = true },
                modifier = Modifier.width(320.dp),
            )

            Box {
                PotiInlineButton(
                    text = "힌트 닫기",
                    onClick = { showHint = false },
                    modifier = Modifier.width(320.dp),
                )

                if (showHint) {
                    HintToolTip()
                }
            }
        }
    }
}

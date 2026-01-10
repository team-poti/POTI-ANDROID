package com.poti.android.core.designsystem.component.field

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties

@Composable
internal fun PotiDropdownMenu(
    expandedState: MutableTransitionState<Boolean>,
    onDismissRequest: () -> Unit,
    parentWidth: Int,
    offset: DpOffset,
    scrollState: ScrollState,
    shape: Shape,
    border: BorderStroke,
    maxHeight: Dp?,
    popupProterties: PopupProperties = PopupProperties(),
    content: @Composable ColumnScope.() -> Unit,
) {
    if (expandedState.currentState || expandedState.targetState) {
        val density = LocalDensity.current
        val popupPositionProvider = remember(offset, density) {
            val offsetYPx = with(density) { offset.y.roundToPx() }
            object : PopupPositionProvider {
                override fun calculatePosition(
                    anchorBounds: IntRect,
                    windowSize: IntSize,
                    layoutDirection: LayoutDirection,
                    popupContentSize: IntSize,
                ): IntOffset {
                    return IntOffset(
                        x = anchorBounds.left,
                        y = anchorBounds.bottom + offsetYPx
                    )
                }
            }
        }

        Popup(
            onDismissRequest = onDismissRequest,
            popupPositionProvider = popupPositionProvider,
            properties = popupProterties,
        ) {
            PotiDropdownMenuContent(
                expandedState = expandedState,
                scrollState = scrollState,
                shape = shape,
                border = border,
                parentWidth = parentWidth,
                maxHeight = maxHeight,
                content = content,
            )
        }
    }
}

@Composable
private fun PotiDropdownMenuContent(
    expandedState: MutableTransitionState<Boolean>,
    scrollState: ScrollState,
    shape: Shape,
    border: BorderStroke?,
    parentWidth: Int,
    maxHeight: Dp?,
    content: @Composable ColumnScope.() -> Unit,
) {
    val density = LocalDensity.current

    AnimatedVisibility(
        visibleState = expandedState,
        enter = slideInVertically(
            initialOffsetY = { -it },
            animationSpec = tween(120, easing = LinearOutSlowInEasing)
        ),
        exit = slideOutVertically(
            targetOffsetY = { -it },
            animationSpec = tween(75, easing = LinearOutSlowInEasing)
        )
    ) {
        Surface(
            modifier = Modifier
                .width(with(density) { parentWidth.toDp() }),
            shape = shape,
            border = border,
        ) {
            Column(
                modifier =
                    Modifier
                        .then(
                            when (maxHeight) {
                                null -> Modifier
                                else -> Modifier.heightIn(max = maxHeight)
                            },
                        )
                        .verticalScroll(scrollState),
                content = content,
            )
        }
    }
}

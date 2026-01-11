package com.poti.android.core.designsystem.component.field

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.poti.android.R
import com.poti.android.core.designsystem.model.FieldMenuItem
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.core.designsystem.theme.White

/**
 * 필드 하단에 드롭다운 메뉴가 제공되는 컴포넌트입니다.
 * 필드에는 텍스트 입력이 불가하며, 필드 터치로 메뉴가 여닫힙니다.
 *
 * @param value 필드에 표시되는 값으로, 유저가 선택한 옵션을 넣어줍니다.
 * @param placeholder 선택한 옵션이 없을 때 필드에 표시됩니다.
 * @param onItemClick 메뉴에서 아이템 클릭 시 호출되는 콜백으로, 클릭한 아이템 객체를 전달합니다.
 * @param menuItems 메뉴에 노출되는 아이템 데이터 리스트입니다.
 * @param selectedIds 메뉴 아이템의 selected 상태 표시에 쓰이며, 외부에서 제어합니다. 선택된 아이템 객체의 id 프로퍼티를 넣어줍니다.
 * @param modifier
 * @param label 필드 상단에 표시됩니다.
 * @param error emptyString이 아닌 경우 필드 하단에 에러 메시지를 노출하고, borderColor를 변경합니다.
 * @param initialOpenState 메뉴의 초기 열림 상태를 설정합니다. 기본값 false로, 열린 상태를 초기값으로 하고 싶을 때에만 true로 설정합니다.
 * @param closeOnItemClick 메뉴 아이템 클릭 시 메뉴를 닫는 옵션입니다. 기본값 true로, 다중 선택 필요하다면 false로 설정합니다.
 * @param maxHeight 메뉴 최대 높이를 제한합니다. 기본값 422dp입니다.
 * @param scrollState 메뉴 스크롤을 외부에서 제어하고 싶을 때 사용합니다.
 * @param offset 필드 하단으로부터 메뉴까지의 간격입니다. 기본값 12dp이며, y값만 조정 가능합니다.
 * @param shape 메뉴 전체 모양입니다.
 * @param border 메뉴 전체 테두리입니다.
 *
 * @author 도연
 * @sample PotiDropdownFieldPreveiw
 */
@Composable
fun PotiDropdownField(
    value: String,
    placeholder: String,
    onItemClick: (FieldMenuItem) -> Unit,
    menuItems: List<FieldMenuItem>,
    selectedIds: Set<String>,
    modifier: Modifier = Modifier,
    label: String = "",
    error: String = "",
    initialOpenState: Boolean = false,
    closeOnItemClick: Boolean = true,
    maxHeight: Dp? = 422.dp,
    scrollState: ScrollState = rememberScrollState(),
    offset: DpOffset = DpOffset(x = 0.dp, y = 12.dp),
    shape: Shape = RoundedCornerShape(8.dp),
    border: BorderStroke = BorderStroke(1.dp, PotiTheme.colors.gray700),
) {
    val expandedState = remember { MutableTransitionState(false) }
    var parentWidth by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        if (initialOpenState) {
            expandedState.targetState = true
        }
    }

    val borderColor = when {
        error.isNotEmpty() -> PotiTheme.colors.sementicRed
        expandedState.currentState || expandedState.targetState -> PotiTheme.colors.gray700
        else -> PotiTheme.colors.gray300
    }

    Box(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FieldLabel(label)

            PotiBasicField(
                value = value,
                onValueChaged = {},
                placeholder = placeholder,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .onGloballyPositioned { coordinates ->
                        parentWidth = coordinates.size.width
                    }
                    .onFocusChanged { focusState ->
                        if (!focusState.isFocused && expandedState.currentState) {
                            expandedState.targetState = false
                        }
                    }
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                    ) {
                        expandedState.targetState = !expandedState.currentState
                    },
                borderColor = borderColor,
                backgroundColor = White,
                trailingIcon = {
                    Crossfade(
                        targetState = expandedState.targetState,
                    ) { opened ->
                        Icon(
                            imageVector = ImageVector.vectorResource(if (opened) R.drawable.ic_arrow_up_lg else R.drawable.ic_arrow_down_lg),
                            contentDescription = null,
                            modifier = Modifier
                                .size(24.dp),
                            tint = PotiTheme.colors.gray700,
                        )
                    }
                },
                enabled = false,
            )

            // TODO: [도연] Display>errorMessage로 대체
            FieldErrorMessage(error)
        }

        PotiDropdownMenu(
            expandedState = expandedState,
            onDismissRequest = {
                expandedState.targetState = false
            },
            scrollState = scrollState,
            parentWidth = parentWidth,
            offset = offset,
            shape = shape,
            border = border,
            maxHeight = maxHeight,
            popupProterties = PopupProperties(
                focusable = true,
            ),
        ) {
            menuItems.forEachIndexed { index, item ->
                PotiMenuItem(
                    option = item.option,
                    onClick = {
                        onItemClick(item)
                        if (closeOnItemClick) {
                            expandedState.targetState = false
                        }
                    },
                    isSelected = item.id in selectedIds,
                    price = item.price,
                    disabled = item.disabled,
                )

                if (index < menuItems.lastIndex) {
                    // TODO: [도연] Display>Divider-sm로 변경
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = PotiTheme.colors.gray300,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PotiDropdownFieldPreveiw() {
    var text by remember { mutableStateOf("") }
    val selectedIds = remember { mutableStateSetOf<String>() }
    val menuItems = listOf(
        FieldMenuItem("옵션"), FieldMenuItem("옵션"), FieldMenuItem("옵션"), FieldMenuItem("옵션"), FieldMenuItem("옵션"), FieldMenuItem("옵션"), FieldMenuItem("옵션"), FieldMenuItem("옵션"), FieldMenuItem("옵션"), FieldMenuItem("옵션"), FieldMenuItem("옵션"), FieldMenuItem("옵션"), FieldMenuItem("옵션"), FieldMenuItem("옵션"), FieldMenuItem("옵션"), FieldMenuItem("옵션"), FieldMenuItem("옵션"), FieldMenuItem("옵션"), FieldMenuItem("옵션"), FieldMenuItem("옵션"), FieldMenuItem("옵션"), FieldMenuItem("옵션"), FieldMenuItem("옵션"), FieldMenuItem("옵션"), FieldMenuItem("옵션"), FieldMenuItem("옵션"), FieldMenuItem("옵션"), FieldMenuItem("옵션"), FieldMenuItem("옵션"), FieldMenuItem("옵션"),
    )

    PotiTheme {
        PotiDropdownField(
            value = text,
            placeholder = "플레이스 홀더",
            initialOpenState = true,
            onItemClick = {
                if (it.id in selectedIds) {
                    selectedIds.remove(it.id)
                    text = ""
                } else {
                    selectedIds.clear()
                    selectedIds.add(it.id)
                    text = it.option
                }
            },
            menuItems = menuItems,
            selectedIds = selectedIds,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(top = 40.dp),
            maxHeight = 600.dp,
        )
    }
}

@Preview
@Composable
private fun PotiDropdownFieldWithPriceWithMutlipleSelectPreveiw() {
    var text by remember { mutableStateOf("") }
    val selectedIds = remember { mutableStateSetOf<String>() }
    val menuItems = listOf(
        FieldMenuItem("옵션", "1,000원"),
        FieldMenuItem("옵션", "1,000원"),
        FieldMenuItem("옵션", "1,000원"),
        FieldMenuItem("옵션", "1,000원"),
        FieldMenuItem("옵션", "1,000원"),
        FieldMenuItem("옵션", "1,000원"),
        FieldMenuItem("옵션", "1,000원"),
        FieldMenuItem("옵션", "1,000원"),
    )

    PotiTheme {
        PotiDropdownField(
            value = text,
            placeholder = "플레이스 홀더",
            initialOpenState = true,
            onItemClick = {
                if (it.id in selectedIds) {
                    selectedIds.remove(it.id)
                    text = ""
                } else {
                    selectedIds.add(it.id)
                    text = it.option
                }
            },
            menuItems = menuItems,
            selectedIds = selectedIds,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(top = 40.dp),
            error = "에러 메시지",
            closeOnItemClick = false,
        )
    }
}

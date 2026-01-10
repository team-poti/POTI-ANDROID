package com.poti.android.core.designsystem.component.field

import android.util.Log
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.designsystem.model.FieldMenuItem
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.core.designsystem.theme.White
import kotlinx.coroutines.delay

@Composable
fun PotiSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    onSearchClick: (String) -> Unit,
    onItemClick: (FieldMenuItem) -> Unit,
    menuItems: List<FieldMenuItem>,
    selectedIds: Set<String>,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester = remember { FocusRequester() },
    scrollState: ScrollState = rememberScrollState(),
    offset: DpOffset = DpOffset(x = 0.dp, y = 12.dp),
    shape: Shape = RoundedCornerShape(8.dp),
    border: BorderStroke = BorderStroke(1.dp, PotiTheme.colors.gray700),
    maxHeight: Dp? = null,
) {
    val expandedState = remember { MutableTransitionState(false) }
    var parentWidth by remember { mutableIntStateOf(0) }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var isFieldFocused by remember { mutableStateOf(false) }
    var searchActionDone by remember { mutableStateOf(false) }
    var dismissRequestDone by remember { mutableStateOf(false) }
    var isTyping by remember { mutableStateOf(false) }

    fun onSearch() {
        onSearchClick(value)
        searchActionDone = true
        Log.d("Search", "👾 searchActionDone=$searchActionDone 세팅 완료")
    }

    fun clearFocusAndHideKeyboard() {
        focusManager.clearFocus()
        keyboardController?.hide()
    }

    LaunchedEffect(isFieldFocused, menuItems.size) {
        if (isFieldFocused) {
            Log.d("Search", "🛸 포커스 있거나 메뉴 길이가 바뀌었다")
            delay(100)
            Log.d("Search", "🛸 잠깐 기다렸어")
            expandedState.targetState = menuItems.isNotEmpty()
            Log.d("Search", "🛸 메뉴 길이에 따라 ${expandedState.targetState}로 설정 완")
        }
    }

    LaunchedEffect(dismissRequestDone) {
        if (dismissRequestDone) {
            Log.d("Search", "👽 dismissRequestDone이어서ㅡLaunchedEffect 실헹")
            delay(100)
            Log.d("Search", "👽 잠깐 기다렸어")
            if (searchActionDone || isTyping) {
                searchActionDone = false
                isTyping = false
                Log.d("Search", "👽 search/typing 했대서 암것도 안 했어")
            } else {
                expandedState.targetState = false
                clearFocusAndHideKeyboard()
                Log.d("Search", "👽 암 것도 아니니까 다 닫았따")
            }
            dismissRequestDone = false
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        PotiBasicField(
            value = value,
            onValueChaged = {
                isTyping = true
                Log.d("Search", "🦝 키보드 입력")
                onValueChange(it)
            },
            placeholder = placeholder,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .onGloballyPositioned { coordinates ->
                    parentWidth = coordinates.size.width
                },
            onFocusChanged = { isFieldFocused = it },
            borderColor = when {
                expandedState.currentState || expandedState.targetState -> PotiTheme.colors.gray700
                else -> PotiTheme.colors.gray300
            },
            backgroundColor = White,
            imeAction = ImeAction.Search,
            onSearchAction = { onSearch() },
            trailingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_search),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                        ) { onSearch() },
                    tint = PotiTheme.colors.gray700,
                )
            },
            focusRequester = focusRequester,
        )

        PotiDropdownMenu(
            expandedState = expandedState,
            onDismissRequest = {
                Log.d("Search", "🚨 onDismissRequest 호출됨!")
                Log.d("Search", "🚨 현재 포커스: $isFieldFocused")
                dismissRequestDone = true
            },
            scrollState = scrollState,
            offset = offset,
            shape = shape,
            border = border,
            maxHeight = maxHeight,
            parentWidth = parentWidth
        ) {
            menuItems.forEachIndexed { index, item ->
                PotiMenuItem(
                    option = item.option,
                    onClick = {
                        onItemClick(item)
                        expandedState.targetState = false
                        clearFocusAndHideKeyboard()
                    },
                    isSelected = item.id in selectedIds,
                    modifier = Modifier.fillMaxWidth(),
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
private fun PotiSearchFieldPreview() {
    var text by remember { mutableStateOf("") }
    val selectedIds = remember { mutableStateSetOf<String>() }
    val menuItems = remember { mutableStateListOf<FieldMenuItem>() }

    PotiTheme {
        PotiSearchField(
            value = text,
            onValueChange = {
                text = it
                menuItems.clear()
                if (text == "hih") {
                    menuItems.add(FieldMenuItem("hih"))
                } else {
                    menuItems.clear()
                }
            },
            placeholder = "플레이스홀더",
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
            onSearchClick = { },
        )
    }
}

package com.poti.android.core.designsystem.component.field

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

/**
 * 필드에 검색어 입력 시, 검색 결과가 드롭다운 메뉴로 제공되는 컴포넌트입니다.
 * 입력에 따른 자동 검색인 경우 외부에서 적절한 menuItems를 넣어주며 제어하며, 명시적인 검색 콜백은 onSearchClick으로 전달합니다.
 *
 *
 * @param value 필드 입력값입니다. 메뉴에서 유저가 아이템 선택 시 선택한 옵션으로 대체합니다.
 * @param onValueChange 필드에 입력된 값을 전달합니다.
 * @param placeholder 입력값 및 선택한 옵션이 없ㅇ르 때 필드에 표시됩니다.
 * @param onSearchClick 검색 콜백입니다.
 * @param onItemClick 메뉴에서 아이템 클릭 시 호출되는 콜백으로, 클릭한 아이템 객체를 전달합니다.
 * @param menuItems 메뉴에 노출되는 아이템 데이터 리스트로, 검색 결과를 넣어줍니다.
 * @param selectedIds 메뉴 아이템의 selected 상태 표시에 쓰이며, 외부에서 제어합니다. 선택된 아이템 객체의 id 프로퍼티를 넣어줍니다.
 * @param searchType 서치 타입에 따라 메뉴 최대 길이가 조정됩니다. 아티스트 검색 시 ARTIST, 상품 등록을 위한 상품명 검색 시 PRODUCT를 사용합니다.
 * @param modifier
 * @param focusRequester 포커스를 외부에서 제어하고 싶을 때 사용합니다. 예: 화면 진입 시 필드에 포커스 가도록
 * @param scrollState 메뉴 스크롤을 외부에서 제어하고 싶을 때 사용합니다.
 * @param offset 필드 하단으로부터 메뉴까지의 간격입니다. 기본값 12dp이며, y값만 조정 가능합니다.
 * @param shape 메뉴 전체 모양입니다.
 * @param border 메뉴 전체 테두리입니다.
 */
@Composable
fun PotiSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    onSearchClick: (String) -> Unit,
    onItemClick: (FieldMenuItem) -> Unit,
    menuItems: List<FieldMenuItem>,
    selectedIds: Set<String>,
    searchType: SearchType,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester = remember { FocusRequester() },
    scrollState: ScrollState = rememberScrollState(),
    offset: DpOffset = DpOffset(x = 0.dp, y = 12.dp),
    shape: Shape = RoundedCornerShape(8.dp),
    border: BorderStroke = BorderStroke(1.dp, PotiTheme.colors.gray700),
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
    }

    fun clearFocusAndHideKeyboard() {
        focusManager.clearFocus()
        keyboardController?.hide()
    }

    LaunchedEffect(isFieldFocused, menuItems.size) {
        if (isFieldFocused) {
            delay(100)
            expandedState.targetState = menuItems.isNotEmpty()
        }
    }

    LaunchedEffect(dismissRequestDone) {
        if (dismissRequestDone) {
            delay(100)
            if (searchActionDone || isTyping) {
                searchActionDone = false
                isTyping = false
            } else {
                expandedState.targetState = false
                clearFocusAndHideKeyboard()
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
                dismissRequestDone = true
            },
            scrollState = scrollState,
            offset = offset,
            shape = shape,
            border = border,
            maxHeight = searchType.maxHeight,
            parentWidth = parentWidth,
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

enum class SearchType(val maxHeight: Dp) {
    ARTIST(500.dp),
    PRODUCT(156.dp)
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
            onSearchClick = { },
            searchType = SearchType.ARTIST
        )
    }
}

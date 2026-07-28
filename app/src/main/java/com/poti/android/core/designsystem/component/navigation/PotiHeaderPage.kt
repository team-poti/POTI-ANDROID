package com.poti.android.core.designsystem.component.navigation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.poti.android.R
import com.poti.android.core.designsystem.component.button.PotiIconButton
import com.poti.android.core.designsystem.component.field.PotiSearchBar
import com.poti.android.core.designsystem.theme.PotiTheme

enum class PotiHeaderPageType(
    @DrawableRes val iconResId: Int,
) {
    BACK(R.drawable.ic_arrow_line_left),
    CLOSE(R.drawable.ic_x),
}

/**
 * @param containerColor 헤더 배경색입니다. 화면 배경이 흰색이 아닌 경우 맞춰서 전달합니다.
 */
@Composable
fun PotiHeaderPage(
    onNavigationClick: () -> Unit,
    modifier: Modifier = Modifier,
    potiHeaderPageType: PotiHeaderPageType = PotiHeaderPageType.BACK,
    title: String? = null,
    subTitle: String? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    @DrawableRes trailingIconRes: Int = R.drawable.ic_switch,
    containerColor: Color = PotiTheme.colors.white,
) {
    Row(
        modifier = modifier
            .background(containerColor)
            .padding(horizontal = 4.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        PotiIconButton(
            iconRes = potiHeaderPageType.iconResId,
            onClick = onNavigationClick,
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            title?.let {
                Text(
                    text = title,
                    style = PotiTheme.typography.body16sb,
                    color = PotiTheme.colors.black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            subTitle?.let {
                Text(
                    text = subTitle,
                    style = PotiTheme.typography.caption12m.copy(
                        lineHeight = 1.35.em,
                    ),
                    color = PotiTheme.colors.gray700,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        onTrailingIconClick?.let {
            PotiIconButton(
                iconRes = trailingIconRes,
                onClick = onTrailingIconClick,
            )
        }
    }
}

/**
 * 검색어 입력이 포함된 헤더입니다.
 *
 * 내비게이션 버튼과 [PotiSearchBar]로 구성되며, 검색 화면 상단에 fixed로 사용합니다.
 *
 * @param onNavigationClick 내비게이션 버튼 클릭 시 호출됩니다.
 * @param value 검색 필드 입력값입니다.
 * @param onValueChange 검색 필드에 입력된 값을 전달합니다.
 * @param onSearch 키보드 검색 액션 또는 검색 아이콘 클릭 시 호출됩니다.
 * @param modifier
 * @param potiHeaderPageType 내비게이션 버튼 아이콘 타입입니다.
 * @param placeholder 입력값이 없을 때 검색 필드에 표시됩니다.
 * @param onClear 삭제 아이콘 클릭 시 호출됩니다.
 * @param focusRequester 포커스를 외부에서 제어하고 싶을 때 사용합니다.
 * @param containerColor 헤더 배경색입니다. 화면 배경이 흰색이 아닌 경우 맞춰서 전달합니다.
 *
 * @sample PotiHeaderPageSearchPreview
 */
@Composable
fun PotiHeaderPageSearch(
    onNavigationClick: () -> Unit,
    value: String,
    onValueChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    potiHeaderPageType: PotiHeaderPageType = PotiHeaderPageType.BACK,
    placeholder: String = stringResource(R.string.search_bar_placeholder),
    onClear: () -> Unit = { onValueChange("") },
    focusRequester: FocusRequester? = null,
    containerColor: Color = PotiTheme.colors.white,
) {
    Row(
        modifier = modifier
            .background(containerColor)
            .padding(horizontal = 4.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        PotiIconButton(
            iconRes = potiHeaderPageType.iconResId,
            onClick = onNavigationClick,
        )

        PotiSearchBar(
            value = value,
            onValueChange = onValueChange,
            onSearch = onSearch,
            modifier = Modifier.weight(1f),
            placeholder = placeholder,
            onClear = onClear,
            focusRequester = focusRequester,
        )
    }
}

@Preview
@Composable
private fun PotiHeaderPagePreview() {
    PotiTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            PotiHeaderPage(
                onNavigationClick = {},
                potiHeaderPageType = PotiHeaderPageType.CLOSE,
            )

            PotiHeaderPage(
                onNavigationClick = {},
            )

            PotiHeaderPage(
                onNavigationClick = {},
                title = "헤더 타이틀",
            )

            PotiHeaderPage(
                onNavigationClick = {},
                title = "헤더 타이틀",
                subTitle = "서브 타이틀",
            )

            PotiHeaderPage(
                onNavigationClick = {},
                title = "헤더 타이틀",
                subTitle = "서브 타이틀",
                onTrailingIconClick = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PotiHeaderPageSearchPreview() {
    var emptyValue by remember { mutableStateOf("") }
    var filledValue by remember { mutableStateOf("아이브") }

    PotiTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            PotiHeaderPageSearch(
                onNavigationClick = {},
                value = emptyValue,
                onValueChange = { emptyValue = it },
                onSearch = {},
            )

            PotiHeaderPageSearch(
                onNavigationClick = {},
                value = filledValue,
                onValueChange = { filledValue = it },
                onSearch = {},
            )
        }
    }
}

package com.poti.android.core.designsystem.component.field

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.common.extension.noRippleClickable
import com.poti.android.core.designsystem.theme.PotiTheme

/**
 * 검색어만 입력받는 단순 검색 바입니다.
 *
 * 입력값이 없으면 검색 아이콘을, 있으면 삭제 아이콘을 노출합니다.
 * 검색 결과를 드롭다운 메뉴로 함께 제공해야 하는 경우에는 [PotiSearchField]를 사용합니다.
 *
 * @param value 필드 입력값입니다.
 * @param onValueChange 필드에 입력된 값을 전달합니다.
 * @param onSearch 키보드 검색 액션 또는 검색 아이콘 클릭 시 호출됩니다.
 * @param modifier
 * @param placeholder 입력값이 없을 때 필드에 표시됩니다.
 * @param onClear 삭제 아이콘 클릭 시 호출됩니다. 기본값은 입력값을 비웁니다.
 * @param focusRequester 포커스를 외부에서 제어하고 싶을 때 사용합니다.
 *
 * @sample PotiSearchBarPreview
 */
@Composable
fun PotiSearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = stringResource(R.string.search_bar_placeholder),
    onClear: () -> Unit = { onValueChange("") },
    focusRequester: FocusRequester? = null,
) {
    val requester = remember { focusRequester ?: FocusRequester() }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .heightIn(min = 48.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(PotiTheme.colors.gray100)
            .focusRequester(requester),
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch(value) }),
        textStyle = PotiTheme.typography.body16m.copy(
            color = PotiTheme.colors.black,
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    innerTextField()

                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = PotiTheme.typography.body16m,
                            color = PotiTheme.colors.gray700,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }

                if (value.isEmpty()) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_search),
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .noRippleClickable { onSearch(value) },
                        tint = PotiTheme.colors.gray700,
                    )
                } else {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_x),
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .noRippleClickable(onClick = onClear),
                        tint = PotiTheme.colors.black,
                    )
                }
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun PotiSearchBarPreview() {
    var emptyValue by remember { mutableStateOf("") }
    var filledValue by remember { mutableStateOf("검색어를 입력하세요") }

    PotiTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            PotiSearchBar(
                value = emptyValue,
                onValueChange = { emptyValue = it },
                onSearch = {},
            )

            PotiSearchBar(
                value = filledValue,
                onValueChange = { filledValue = it },
                onSearch = {},
            )
        }
    }
}

package com.poti.android.core.designsystem.component.field

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.core.common.extension.noRippleClickable
import com.poti.android.core.designsystem.component.display.PotiErrorMessage
import com.poti.android.core.designsystem.theme.PotiTheme

/**
 * 직접 입력하지 않고 클릭을 통해 값을 선택하는 필드입니다.
 * 라벨과 에러 메시지를 제외한 필드 영역만 클릭할 수 있습니다.
 *
 * @param value 필드에 표시되는 값입니다.
 * @param placeholder 값이 없을 때 표시됩니다.
 * @param onClick 필드 클릭 시 실행할 콜백입니다.
 * @param modifier
 * @param enabled 클릭 가능 여부입니다.
 * @param label 필드 상단에 표시됩니다.
 * @param error 에러가 emptyString이 아닌 경우 필드 하단에 표시되며, borderColor가 red로 변경됩니다.
 * @param trailingIcon 필드 우측에 표시되는 아이콘입니다.
 */
@Composable
fun PotiClickableField(
    value: String,
    placeholder: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String = "",
    error: String = "",
    trailingIcon: (@Composable () -> Unit)? = null,
) {
    val status = if (error.isNotEmpty()) FieldStatus.ERROR else FieldStatus.DEFAULT
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FieldLabel(label)

        PotiBasicField(
            value = value,
            onValueChanged = {},
            placeholder = placeholder,
            borderColor = status.borderColor,
            backgroundColor = PotiTheme.colors.white,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(52.dp)
                .noRippleClickable(
                    interactionSource = interactionSource,
                    enabled = enabled,
                    onClick = onClick,
                ),
            enabled = false,
            singleLine = true,
            trailingIcon = trailingIcon,
        )

        if (error.isNotBlank()) {
            PotiErrorMessage(message = error)
        }
    }
}

@Preview
@Composable
private fun PotiClickableFieldPreview() {
    PotiTheme {
        PotiClickableField(
            value = "01234",
            placeholder = "우편번호",
            label = "우편번호",
            onClick = {},
        )
    }
}

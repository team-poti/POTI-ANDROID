package com.poti.android.core.designsystem.component.modal

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.common.extension.heightForScreenPercentage
import com.poti.android.core.common.util.screenWidthDp
import com.poti.android.core.designsystem.component.button.ModalButtonType
import com.poti.android.core.designsystem.component.button.PotiModalButton
import com.poti.android.core.designsystem.theme.PotiTheme

/**
 * 라지 모달 컴포넌트입니다. 기본 모달 컴포넌트를 래핑해, 디자인 요구사항에 따른 기본 레이아웃을 제공합니다.
 *
 * @param onDismissRequest 모달을 닫는 콜백입니다.
 * @param title "큰 내용"에 해당하는 텍스트입니다.
 * @param text "작은 내용"에 해당하는 텍스트입니다.
 * @param btnText Main 버튼 텍스트입니다.
 * @param onBtnClick Main 버튼 클릭 콜백입니다.
 * @param modifier
 * @param subBtnText Sub 버튼 텍스트입니다. lg-1일 때 사용합니다.
 * @param onSubBtnClick Sub 버튼 클릭 콜백입니다. lg-1일 때 사용합니다.
 * @param image 이미지 에셋 아이디입니다. lg-2일 때 사용합니다.
 * @param dismissOnBackPress 시스템 뒤로가기 시 모달을 닫는 여부입니다. 기본값 true입니다.
 * @param dismissOnClickOutside 모달 바깥 영역 터치 시 모달을 닫는 여부입니다. 기본값 true입니다.
 * @param content 텍스트~버튼 사이 공간에 배치됩니다. lg-1일 때 사용합니다.
 *
 * @author
 * @sample PotiLarge1Preview
 */
@Composable
fun PotiLargeModal(
    onDismissRequest: () -> Unit,
    title: String,
    text: String,
    btnText: String,
    onBtnClick: () -> Unit,
    modifier: Modifier = Modifier,
    subBtnText: String? = null,
    onSubBtnClick: (() -> Unit)? = null,
    @DrawableRes image: Int? = null,
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true,
    content: (@Composable ColumnScope.() -> Unit)? = null,
) {
    val hasSubBtn = subBtnText != null && onSubBtnClick != null

    PotiModal(
        onDismissRequest = onDismissRequest,
        modifier = modifier.padding(horizontal = screenWidthDp(36.dp)),
        dismissOnBackPress = dismissOnBackPress,
        dismissOnClickOutside = dismissOnClickOutside,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = screenWidthDp(16.dp))
                .padding(top = 36.dp, bottom = if (hasSubBtn) 12.dp else 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = title,
                modifier = Modifier
                    .padding(bottom = 8.dp),
                color = PotiTheme.colors.black,
                style = PotiTheme.typography.title18sb,
            )

            Text(
                text = text,
                color = PotiTheme.colors.gray800,
                style = PotiTheme.typography.body14m,
            )

            content?.invoke(this)

            image?.let {
                Image(
                    painter = painterResource(id = it),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(vertical = 24.dp)
                        .heightForScreenPercentage(180.dp)
                        .aspectRatio(1f),
                    contentScale = ContentScale.Crop,
                )
            }

            Column {
                PotiModalButton(
                    text = btnText,
                    onClick = onBtnClick,
                    modifier = Modifier.fillMaxWidth(),
                    type = ModalButtonType.MAIN,
                )

                if (hasSubBtn) {
                    PotiModalButton(
                        text = subBtnText,
                        onClick = onSubBtnClick,
                        modifier = Modifier.fillMaxWidth(),
                        type = ModalButtonType.SUB_2,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PotiLarge1Preview() {
    PotiTheme {
        PotiLargeModal(
            onDismissRequest = {},
            title = "큰 내용",
            text = "작은 내용",
            btnText = "확인",
            onBtnClick = {},
            subBtnText = "넘어갈래요",
            onSubBtnClick = {},
        ) {
        }
    }
}

@Preview
@Composable
private fun PotiLarge2Preview() {
    PotiTheme {
        PotiLargeModal(
            onDismissRequest = {},
            title = "큰 내용",
            text = "작은 내용",
            btnText = "확인",
            onBtnClick = {},
            image = R.drawable.ic_launcher_background,
        )
    }
}

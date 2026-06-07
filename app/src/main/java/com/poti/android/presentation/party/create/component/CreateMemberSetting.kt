package com.poti.android.presentation.party.create.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.designsystem.component.button.PotiInlineButton
import com.poti.android.core.designsystem.component.display.PotiEmptyStateInline
import com.poti.android.core.designsystem.component.display.PotiErrorMessage
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.domain.model.artist.MemberPriceOption
import com.poti.android.presentation.party.create.model.MemberSettingStatus
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun CreateMemberSetting(
    status: MemberSettingStatus,
    selectedMembers: ImmutableList<MemberPriceOption>,
    onPriceChange: (MemberPriceOption) -> Unit,
    onEditBtnClick: () -> Unit,
    layoutBottom: Float,
    errorMessage: String,
    modifier: Modifier = Modifier,
) {
    var isEditBtnInLayout by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    val isKeyboardVisible = WindowInsets.ime.getBottom(density) > 0

    var hideHint by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .padding(vertical = 24.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = stringResource(R.string.create_title_member_setting),
                color = PotiTheme.colors.black,
                style = PotiTheme.typography.title18sb,
            )

            if (errorMessage.isNotEmpty()) {
                PotiErrorMessage(errorMessage)
            }
        }

        when (status) {
            MemberSettingStatus.ARTIST_NOT_SELECTED -> PotiEmptyStateInline(stringResource(R.string.create_placeholder_need_artist))
            MemberSettingStatus.MEMBER_NOT_SELECTED -> PotiEmptyStateInline(stringResource(R.string.create_placeholder_need_member))
            MemberSettingStatus.EDITABLE -> {
                Column {
                    selectedMembers.forEachIndexed { index, option ->
                        val isLastOption = index == selectedMembers.size - 1

                        EditOptionPrice(
                            option = option.name,
                            value = option.price,
                            onValueChanged = { newPrice ->
                                val newOption = option.copy(price = newPrice)
                                onPriceChange(newOption)
                            },
                            modifier = Modifier.padding(bottom = if (isLastOption) 0.dp else 20.dp),
                            imeAction = if (isLastOption) ImeAction.Done else ImeAction.Next,
                            onFocusChanged = { focused ->
                                if (focused) {
                                    hideHint = true
                                }
                            },
                        )
                    }
                }
            }
        }

        if (status != MemberSettingStatus.ARTIST_NOT_SELECTED) {
            Box {
                PotiInlineButton(
                    text = stringResource(R.string.create_btn_member_edit),
                    onClick = {
                        hideHint = true
                        onEditBtnClick()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            val top = coordinates.positionInWindow().y
                            val isVisible = (top < layoutBottom) && (top > 0)
                            if (isEditBtnInLayout != isVisible) {
                                isEditBtnInLayout = isVisible
                            }
                        },
                )

                if (!hideHint && isEditBtnInLayout && !isKeyboardVisible) {
                    HintToolTip()
                }
            }
        }
    }
}

@Preview
@Composable
private fun CreateMemberSettingPreview() {
    val scrollState = rememberScrollState()

    PotiTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 80.dp),
            verticalArrangement = Arrangement.spacedBy(40.dp),
        ) {
            CreateMemberSetting(
                status = MemberSettingStatus.ARTIST_NOT_SELECTED,
                selectedMembers = persistentListOf(),
                onPriceChange = {},
                onEditBtnClick = {},
                layoutBottom = 0f,
                errorMessage = "",
            )

            CreateMemberSetting(
                status = MemberSettingStatus.MEMBER_NOT_SELECTED,
                selectedMembers = persistentListOf(),
                onPriceChange = {},
                onEditBtnClick = {},
                layoutBottom = 0f,
                errorMessage = "",
            )

            CreateMemberSetting(
                status = MemberSettingStatus.EDITABLE,
                selectedMembers = persistentListOf(
                    MemberPriceOption(memberId = 1, name = "원영", price = "5000"),
                    MemberPriceOption(memberId = 1, name = "유진", price = ""),
                    MemberPriceOption(memberId = 1, name = "레이", price = "4000"),
                ),
                onPriceChange = {},
                onEditBtnClick = {},
                layoutBottom = 0f,
                errorMessage = "모든 멤버에 가격을 설정해주세요",
            )
        }
    }
}

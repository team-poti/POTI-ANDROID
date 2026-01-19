package com.poti.android.presentation.party.create.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.platform.LocalConfiguration
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

@Composable
fun CreateMemberSetting(
    status: MemberSettingStatus,
    selectedMembersOption: List<MemberPriceOption>,
    onPriceChange: (MemberPriceOption) -> Unit,
    onEditBtnClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showHint by remember(status) {
        mutableStateOf(status != MemberSettingStatus.DEFAULT)
    }

    var isEditBtnInScreen by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current

    val screenHeight = remember(configuration.screenHeightDp) {
        with(density) {
            configuration.screenHeightDp.dp.roundToPx()
        }
    }

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

            when (status) {
                MemberSettingStatus.ERROR_NO_MEMBER -> PotiErrorMessage(stringResource(R.string.create_msg_need_member))
                MemberSettingStatus.ERROR_NO_PRICE -> PotiErrorMessage(stringResource(R.string.create_msg_need_price))
                else -> Unit
            }
        }

        when {
            status == MemberSettingStatus.DEFAULT -> PotiEmptyStateInline(stringResource(R.string.create_placeholder_need_artist))
            selectedMembersOption.isEmpty() -> PotiEmptyStateInline(stringResource(R.string.create_placeholder_need_member))
            else -> {
                Column {
                    selectedMembersOption.forEachIndexed { index, option ->
                        val isLastOption = index == selectedMembersOption.size - 1

                        EditOptionPrice(
                            option = option.name,
                            value = option.price,
                            onValueChanged = { newPrice ->
                                val newOption = MemberPriceOption(
                                    memberId = option.memberId,
                                    name = option.name,
                                    price = newPrice,
                                )
                                onPriceChange(newOption)
                            },
                            modifier = Modifier.padding(bottom = if (isLastOption) 0.dp else 20.dp),
                            imeAction = if (isLastOption) ImeAction.Done else ImeAction.Next,
                            onFocusChanged = { focused ->
                                if (focused) {
                                    showHint = false
                                }
                            },
                        )
                    }
                }
            }
        }

        if (status != MemberSettingStatus.DEFAULT) {
            Box {
                PotiInlineButton(
                    text = stringResource(R.string.create_btn_member_edit),
                    onClick = {
                        showHint = false
                        onEditBtnClick()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            val buttonTop = coordinates.positionInWindow().y
                            isEditBtnInScreen = buttonTop < screenHeight
                        },
                )

                if (showHint && isEditBtnInScreen) {
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
                status = MemberSettingStatus.DEFAULT,
                selectedMembersOption = emptyList(),
                onPriceChange = {},
                onEditBtnClick = {},
            )

            CreateMemberSetting(
                status = MemberSettingStatus.IN_PROGRESS,
                selectedMembersOption = emptyList(),
                onPriceChange = {},
                onEditBtnClick = {},
            )

            CreateMemberSetting(
                status = MemberSettingStatus.IN_PROGRESS,
                selectedMembersOption = listOf(
                    MemberPriceOption(memberId = 1, name = "원영", price = "5000"),
                    MemberPriceOption(memberId = 1, name = "유진", price = ""),
                    MemberPriceOption(memberId = 1, name = "레이", price = "4000"),
                ),
                onPriceChange = {},
                onEditBtnClick = {},
            )

            CreateMemberSetting(
                status = MemberSettingStatus.ERROR_NO_MEMBER,
                selectedMembersOption = emptyList(),
                onPriceChange = {},
                onEditBtnClick = {},
            )

            CreateMemberSetting(
                status = MemberSettingStatus.ERROR_NO_PRICE,
                selectedMembersOption = listOf(
                    MemberPriceOption(memberId = 1, name = "원영", price = "5000"),
                    MemberPriceOption(memberId = 1, name = "유진", price = ""),
                    MemberPriceOption(memberId = 1, name = "레이", price = "4000"),
                ),
                onPriceChange = {},
                onEditBtnClick = {},
            )
        }
    }
}

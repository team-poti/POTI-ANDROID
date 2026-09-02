package com.poti.android.presentation.party.detail.model

import com.poti.android.core.common.state.ApiState
import com.poti.android.data.mock.UiMockData
import com.poti.android.domain.type.PartyStatusType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PartyDetailUiStateTest {
    @Test
    fun `내가 등록한 분철팟은 모집 중이어도 참여할 수 없다`() {
        val state = PartyDetailUiState(
            partyDetail = ApiState.Success(UiMockData.partyDetail.copy(isMyPost = true)),
        )

        assertFalse(state.isDetailJoinEnable)
        assertEquals(PartyDetailJoinButtonState.MY_POST, state.detailJoinButtonState)
    }

    @Test
    fun `이미 참여한 분철팟은 모집 중이어도 참여할 수 없다`() {
        val state = PartyDetailUiState(
            partyDetail = ApiState.Success(UiMockData.partyDetail.copy(isParticipated = true)),
        )

        assertFalse(state.isDetailJoinEnable)
        assertEquals(PartyDetailJoinButtonState.ALREADY_JOINED, state.detailJoinButtonState)
    }

    @Test
    fun `모집 중이고 작성자나 참여자가 아니면 참여할 수 있다`() {
        val state = PartyDetailUiState(
            partyDetail = ApiState.Success(UiMockData.partyDetail),
        )

        assertTrue(state.isDetailJoinEnable)
    }

    @Test
    fun `모집이 끝났으면 작성자나 참여자가 아니어도 참여할 수 없다`() {
        val state = PartyDetailUiState(
            partyDetail = ApiState.Success(UiMockData.partyDetail.copy(status = PartyStatusType.CLOSED)),
        )

        assertFalse(state.isDetailJoinEnable)
    }

    @Test
    fun `비로그인 사용자는 모집 중인 분철팟에 참여 버튼이 활성화된다`() {
        val state = PartyDetailUiState(
            partyDetail = ApiState.Success(UiMockData.partyDetail),
        )

        assertTrue(state.isDetailJoinEnable)
    }
}

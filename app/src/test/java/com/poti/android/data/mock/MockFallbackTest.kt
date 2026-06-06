package com.poti.android.data.mock

import com.poti.android.core.network.model.NetworkError
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test

class MockFallbackTest {
    @Test
    fun `mock mode replaces network result with UI mock`() = runBlocking {
        val result = Result.failure<Int>(NetworkError.NetworkConnection)
            .useUiMockWhenEnabled(useMock = true) { 42 }

        assertEquals(42, result.getOrThrow())
    }

    @Test
    fun `debug mode keeps network error`() = runBlocking {
        val error = NetworkError.BadRequest(code = 40001, serverMsg = "invalid request")
        val result = Result.failure<Int>(error)
            .useUiMockWhenEnabled(useMock = false) { 42 }

        assertSame(error, result.exceptionOrNull())
    }

    @Test
    fun `mock mode replaces successful server result`() = runBlocking {
        val result = Result.success(10)
            .useUiMockWhenEnabled(useMock = true) { 42 }

        assertEquals(42, result.getOrThrow())
    }

    @Test
    fun `UI mock data initializes nested history models`() {
        assertEquals(2, UiMockData.recruiterDetail.participantCount)
        assertEquals("IVE", UiMockData.participantDetail.partySummary.artist)
    }
}

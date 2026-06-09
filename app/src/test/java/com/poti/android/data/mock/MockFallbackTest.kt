package com.poti.android.data.mock

import com.poti.android.core.network.model.NetworkError
import kotlinx.coroutines.CancellationException
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

    @Test(expected = CancellationException::class)
    fun `mock mode rethrows cancellation exception`() {
        runBlocking {
            Result.success(10)
                .useUiMockWhenEnabled(useMock = true) {
                    throw CancellationException("cancelled")
                }
        }
    }

    @Test
    fun `mock mode wraps non cancellation exception in failure`() = runBlocking {
        val error = IllegalStateException("mock failed")
        val result = Result.success(10)
            .useUiMockWhenEnabled(useMock = true) { throw error }

        assertSame(error, result.exceptionOrNull())
    }

    @Test
    fun `mock write mode does not execute real call`() = runBlocking {
        var realCallCount = 0

        val result = executeWithUiMock(
            useMock = true,
            mock = { 42 },
            real = {
                realCallCount += 1
                Result.success(10)
            },
        )

        assertEquals(42, result.getOrThrow())
        assertEquals(0, realCallCount)
    }

    @Test
    fun `debug write mode executes only real call`() = runBlocking {
        var mockCallCount = 0

        val result = executeWithUiMock(
            useMock = false,
            mock = {
                mockCallCount += 1
                42
            },
            real = { Result.success(10) },
        )

        assertEquals(10, result.getOrThrow())
        assertEquals(0, mockCallCount)
    }

    @Test(expected = CancellationException::class)
    fun `mock write mode rethrows cancellation exception`() {
        runBlocking {
            executeWithUiMock(
                useMock = true,
                mock = { throw CancellationException("cancelled") },
                real = { Result.success(10) },
            )
        }
    }

    @Test
    fun `UI mock data initializes nested history models`() {
        assertEquals(2, UiMockData.recruiterDetail.participantCount)
        assertEquals("IVE", UiMockData.participantDetail.partySummary.artist)
    }
}

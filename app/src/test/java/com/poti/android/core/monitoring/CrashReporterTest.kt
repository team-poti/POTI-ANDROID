package com.poti.android.core.monitoring

import com.poti.android.core.network.model.ApiBusinessException
import com.poti.android.core.network.model.BaseResponse
import com.poti.android.core.network.model.MissingResponseDataException
import com.poti.android.core.network.model.NetworkError
import com.poti.android.core.network.model.handleApiResponse
import com.poti.android.core.network.util.HttpResponseHandler
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

class CrashReporterTest {
    @Test
    fun `expected failures are excluded`() {
        listOf(IOException("offline"), CancellationException(), NetworkError.Unknown, ApiBusinessException("conflict"))
            .forEach { assertFalse(isUnexpectedFailure(it)) }
        assertTrue(isUnexpectedFailure(SerializationException("body")))
        assertTrue(isUnexpectedFailure(MissingResponseDataException()))
    }

    @Test
    fun `sanitizer removes sensitive messages and nested exceptions`() {
        val original = IllegalStateException("secret token", IOException("private URL"))
        original.addSuppressed(Exception("address"))
        val safe = sanitizedException(original, CrashOperation.API_RESPONSE)
        assertEquals("API_RESPONSE: IllegalStateException", safe.message)
        assertNull(safe.cause)
        assertTrue(safe.suppressed.isEmpty())
        assertTrue(original.stackTrace.contentEquals(safe.stackTrace))
    }

    @Test
    fun `handler reports unexpected failure once before mapping`() = runBlocking {
        val reports = mutableListOf<Throwable>()
        val reporter = object : CrashReporter {
            override fun recordNonFatal(
                error: Throwable,
                operation: CrashOperation,
            ) {
                reports.add(error)
            }
        }
        val handler = HttpResponseHandler(Json, reporter)
        val error = SerializationException("sensitive body")
        assertEquals(NetworkError.Unknown, handler.safeApiCall<Unit> { throw error }.exceptionOrNull())
        handler.safeApiCall<Unit> { throw IOException() }
        handler.safeApiCall<Unit> { throw ApiBusinessException("expected") }
        handler.safeApiCall { BaseResponse<String>(200, "ok").handleApiResponse().getOrThrow() }
        assertEquals(2, reports.size)
        assertTrue(reports[0] === error)
        assertTrue(reports[1] is MissingResponseDataException)
    }
}

package com.poti.android.core.common.extension

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.poti.android.R
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit

private const val MINUTES_PER_HOUR = 60L
private const val MINUTES_PER_DAY = MINUTES_PER_HOUR * 24
private const val MINUTES_PER_WEEK = MINUTES_PER_DAY * 7

private val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
private val MONTH_DAY_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("MM.dd")

private val SERVER_OFFSET: ZoneOffset = ZoneOffset.UTC

fun String.toPartyUploadDate(): String? {
    return try {
        val dateTime = LocalDateTime.parse(this)
        dateTime.format(DATE_FORMATTER)
    } catch (_: DateTimeParseException) {
        null
    }
}

/**
 * 서버가 내려주는 시각을 상대 시간 문구로 변환합니다.
 * 오프셋이 있는 형식(2026-08-19T08:30:16.135Z)과 없는 형식(2026-08-19T18:35:19.965804)을 모두 처리하며,
 * 7일이 지나면 MM.DD 형식 날짜로, 파싱에 실패하면 원본 문자열을 그대로 표시합니다.
 */
@Composable
fun String.toRelativeTime(): String {
    val dateTime = toServerDateTime()
        ?.atZoneSameInstant(ZoneId.systemDefault())
        ?.toLocalDateTime()
        ?: return this

    val minutes = ChronoUnit.MINUTES.between(dateTime, LocalDateTime.now())

    return when {
        minutes < 1 -> stringResource(R.string.time_just_now)
        minutes < MINUTES_PER_HOUR -> stringResource(R.string.time_minutes_ago, minutes)
        minutes < MINUTES_PER_DAY -> stringResource(R.string.time_hours_ago, minutes / MINUTES_PER_HOUR)
        minutes < MINUTES_PER_WEEK -> stringResource(R.string.time_days_ago, minutes / MINUTES_PER_DAY)
        else -> dateTime.format(MONTH_DAY_FORMATTER)
    }
}

private fun String.toServerDateTime(): OffsetDateTime? =
    runCatching { OffsetDateTime.parse(this) }
        .recoverCatching { LocalDateTime.parse(this).atOffset(SERVER_OFFSET) }
        .getOrNull()

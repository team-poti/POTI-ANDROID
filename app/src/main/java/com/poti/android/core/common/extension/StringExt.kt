package com.poti.android.core.common.extension

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.poti.android.R
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit

private const val MINUTES_PER_HOUR = 60L
private const val MINUTES_PER_DAY = MINUTES_PER_HOUR * 24
private const val MINUTES_PER_WEEK = MINUTES_PER_DAY * 7

private val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
private val MONTH_DAY_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("MM.dd")

fun String.toPartyUploadDate(): String? {
    return try {
        val dateTime = LocalDateTime.parse(this)
        dateTime.format(DATE_FORMATTER)
    } catch (_: DateTimeParseException) {
        null
    }
}

/**
 * 서버가 내려주는 시각(2026-08-19T08:30:16.135Z)을 상대 시간 문구로 변환합니다.
 * 7일이 지나면 MM.DD 형식 날짜로, 파싱에 실패하면 원본 문자열을 그대로 표시합니다.
 */
@Composable
fun String.toRelativeTime(): String {
    val dateTime = runCatching {
        OffsetDateTime.parse(this)
            .atZoneSameInstant(ZoneId.systemDefault())
            .toLocalDateTime()
    }.getOrNull() ?: return this

    val minutes = ChronoUnit.MINUTES.between(dateTime, LocalDateTime.now())

    return when {
        minutes < 1 -> stringResource(R.string.time_just_now)
        minutes < MINUTES_PER_HOUR -> stringResource(R.string.time_minutes_ago, minutes)
        minutes < MINUTES_PER_DAY -> stringResource(R.string.time_hours_ago, minutes / MINUTES_PER_HOUR)
        minutes < MINUTES_PER_WEEK -> stringResource(R.string.time_days_ago, minutes / MINUTES_PER_DAY)
        else -> dateTime.format(MONTH_DAY_FORMATTER)
    }
}

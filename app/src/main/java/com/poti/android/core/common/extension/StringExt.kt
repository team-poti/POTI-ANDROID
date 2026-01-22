package com.poti.android.core.common.extension

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

fun String.toPartyUploadDate(): String? {
    return try {
        val dateTime = LocalDateTime.parse(this)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        dateTime.format(formatter)
    } catch (_: DateTimeParseException) {
        null
    }
}

package com.poti.android.presentation.party.create.util

import android.util.Log
import androidx.core.text.isDigitsOnly
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

fun String.toDateOrNull(): LocalDate? {
    return try {
        if (!this.isDigitsOnly() || this.length != 8) return null
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        return LocalDate.parse(this, formatter)
    } catch (_: DateTimeParseException) {
        null
    }
}

fun LocalDate.isTodayOrAfter(): Boolean {
    val today = LocalDate.now()
    return this.isAfter(today) || this.isEqual(today)
}

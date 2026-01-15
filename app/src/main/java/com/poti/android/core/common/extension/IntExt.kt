package com.poti.android.core.common.extension

import java.text.DecimalFormat

fun Int.toMoneyString(): String {
    val decimalFormat = DecimalFormat("#,###")
    return decimalFormat.format(this)
}

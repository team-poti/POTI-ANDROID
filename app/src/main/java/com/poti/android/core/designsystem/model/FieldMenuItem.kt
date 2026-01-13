package com.poti.android.core.designsystem.model

import java.util.UUID

data class FieldMenuItem(
    val option: String,
    val price: String? = null,
    val disabled: Boolean = false,
    val id: String = UUID.randomUUID().toString(),
)

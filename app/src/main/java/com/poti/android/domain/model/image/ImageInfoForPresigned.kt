package com.poti.android.domain.model.image

import java.io.File

data class ImageInfoForPresigned(
    val file: File,
    val extension: String,
)

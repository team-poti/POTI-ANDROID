package com.poti.android.domain.repository

import java.io.File

interface FileUploadRepository {
    suspend fun uploadImage(
        uploadUrl: String,
        file: File,
    ): Result<Unit>

    fun createImage(uriString: String): Result<File>

    fun clearDirectory(): Result<Unit>
}

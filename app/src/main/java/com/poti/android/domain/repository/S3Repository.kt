package com.poti.android.domain.repository

import com.poti.android.domain.model.image.PresignedUploadInfo
import java.io.File

interface S3Repository {
    suspend fun uploadImages(
        uploadInfos: List<PresignedUploadInfo>,
        files: List<File>,
        extensions: List<String>,
    ): Result<Unit>

    suspend fun uploadSingleImage(
        presignedUrl: String,
        file: File,
        extension: String,
    ): Result<Unit>
}

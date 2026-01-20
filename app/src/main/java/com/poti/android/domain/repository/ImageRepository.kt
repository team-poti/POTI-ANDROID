package com.poti.android.domain.repository

import com.poti.android.domain.model.image.PresignedUploadInfo

interface ImageRepository {
    suspend fun getPresignedUrls(
        type: String,
        count: Int,
        extension: String,
    ): Result<List<PresignedUploadInfo>>
}

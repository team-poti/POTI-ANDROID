package com.poti.android.data.mapper.image

import com.poti.android.data.remote.dto.response.image.PresignedUrlListResponseDto
import com.poti.android.domain.model.image.PresignedUploadInfo

fun PresignedUrlListResponseDto.toDomain(): List<PresignedUploadInfo> = urls.map {
    PresignedUploadInfo(
        fileName = it.fileName,
        url = it.url,
    )
}

package com.poti.android.domain.usecase.image

import javax.inject.Inject

class UploadImagesUseCase @Inject constructor(
) {
    suspend operator fun invoke() {
        // TODO: [도연] presigned URL 발급 및 S3 이미지 업로드
    }
}

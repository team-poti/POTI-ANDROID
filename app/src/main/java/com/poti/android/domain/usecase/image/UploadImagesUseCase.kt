package com.poti.android.domain.usecase.image

import com.poti.android.domain.model.image.ImageInfoForPresigned
import com.poti.android.domain.repository.ImageRepository
import com.poti.android.domain.repository.S3Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UploadImagesUseCase @Inject constructor(
    private val imageRepository: ImageRepository,
    private val s3Repository: S3Repository,
) {
    suspend operator fun invoke(
        type: String,
        imageInfos: List<ImageInfoForPresigned>,
    ): Result<List<String>> = withContext(Dispatchers.IO) {
        val extensions = imageInfos.map { it.extension }

        imageRepository.getPresignedUrls(type, extensions)
            .mapCatching { presignedInfos ->

                presignedInfos.zip(imageInfos).forEach { (presigned, imageInfo) ->
                    s3Repository.uploadSingleImage(
                        presignedUrl = presigned.url,
                        file = imageInfo.file,
                        extension = imageInfo.extension,
                    ).getOrThrow()
                }

                presignedInfos.map { it.fileName }
            }
    }
}

package com.poti.android.domain.usecase.image

import com.poti.android.core.common.constant.ImageConstants.IMAGE_EXTENSION
import com.poti.android.domain.model.image.PresignedUploadInfo
import com.poti.android.domain.repository.FileUploadRepository
import com.poti.android.domain.repository.ImageRepository
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class UploadImagesUseCase @Inject constructor(
    private val imageRepository: ImageRepository,
    private val fileUploadRepository: FileUploadRepository,
) {
    suspend operator fun invoke(
        uploadType: String,
        uriStrings: List<String>,
    ): Result<List<String>> {
        try {
            val uploadInfos = getUploadUrls(uploadType, uriStrings.size)
            val (urls, fileNames) = uploadInfos.map { it.url to it.fileName }.unzip()
            val files = createImages(uriStrings)

            uploadImages(urls, files)

            return Result.success(fileNames)
        } catch (t: Throwable) {
            if (t is CancellationException) throw t
            return Result.failure(t)
        } finally {
            fileUploadRepository.clearDirectory()
        }
    }

    private suspend fun getUploadUrls(
        uploadType: String,
        size: Int,
    ): List<PresignedUploadInfo> = imageRepository.getPresignedUrls(
        type = uploadType,
        extensions = List(size) { IMAGE_EXTENSION },
    ).getOrThrow()

    private fun createImages(
        uriStrings: List<String>,
    ): List<File> = uriStrings.map { uri ->
        fileUploadRepository.createImage(uri).getOrThrow()
    }

    private suspend fun uploadImages(
        urls: List<String>,
        files: List<File>,
    ) {
        if (urls.size != files.size) {
            throw IllegalStateException("Upload URL count and file count must match")
        }

        for (i in urls.indices) {
            fileUploadRepository.uploadImage(urls[i], files[i]).getOrThrow()
        }
    }
}

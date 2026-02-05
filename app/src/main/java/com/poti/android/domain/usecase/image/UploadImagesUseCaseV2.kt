package com.poti.android.domain.usecase.image

import com.poti.android.core.common.constant.ImageConstants.IMAGE_EXTENSION
import com.poti.android.domain.model.image.PresignedUploadInfo
import com.poti.android.domain.repository.FileUplaodRepository
import com.poti.android.domain.repository.ImageRepository
import java.io.File
import javax.inject.Inject

class UploadImagesUseCaseV2 @Inject constructor(
    private val imageRepository: ImageRepository,
    private val fileUplaodRepository: FileUplaodRepository,
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
            clearDirectory()

            return Result.success(fileNames)
        } catch (t: Throwable) {
            return Result.failure(t)
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
        fileUplaodRepository.createImage(uri).getOrThrow()
    }

    private suspend fun uploadImages(
        urls: List<String>,
        files: List<File>,
    ) = urls.zip(files) { url, file ->
        fileUplaodRepository.uploadImage(url, file).getOrThrow()
    }

    private fun clearDirectory() = fileUplaodRepository
        .clearDirectory().getOrThrow()
}

package com.poti.android.domain.usecase.image

import com.poti.android.data.di.ImageUpload
import com.poti.android.domain.model.image.ImageInfoForPresigned
import com.poti.android.domain.model.image.PresignedUploadInfo
import com.poti.android.domain.repository.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import javax.inject.Inject

class UploadImagesUseCase @Inject constructor(
    @param:ImageUpload private val okHttpClient: OkHttpClient,
    private val imageRepository: ImageRepository,
) {
    suspend operator fun invoke(
        type: String,
        imageInfos: List<ImageInfoForPresigned>,
    ): Result<List<String>> = withContext(Dispatchers.IO) {
        getPresignedUrl(type, imageInfos)
            .mapCatching { presignedInfos ->
                presignedInfos.zip(imageInfos).map { (presigned, imageInfo) ->
                    uploadImage(
                        url = presigned.url,
                        file = imageInfo.file,
                        extension = imageInfo.extension,
                    )
                    presigned.fileName
                }
            }
    }

    private suspend fun getPresignedUrl(type: String, imageInfos: List<ImageInfoForPresigned>): Result<List<PresignedUploadInfo>> {
        val extensions = imageInfos.map { info -> info.extension }

        return imageRepository.getPresignedUrls(type, extensions)
    }

    private fun contentTypeFor(extension: String): String =
        when (extension.lowercase()) {
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            "webp" -> "image/webp"
            else -> "application/octet-stream"
        }

    private fun uploadImage(
        url: String,
        file: File,
        extension: String,
    ) {
        val mediaType = contentTypeFor(extension).toMediaType()

        val requestBody = file.asRequestBody(mediaType)

        val request = Request.Builder()
            .url(url)
            .put(requestBody)
            .header("Content-Type", mediaType.toString())
            .build()

        okHttpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw IOException("Upload failed: ${response.code}")
            }
        }
    }
}

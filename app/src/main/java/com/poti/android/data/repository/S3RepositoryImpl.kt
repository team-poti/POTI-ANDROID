package com.poti.android.data.repository

import com.poti.android.data.di.S3UploadClient
import com.poti.android.domain.model.image.PresignedUploadInfo
import com.poti.android.domain.repository.S3Repository
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class S3RepositoryImpl @Inject constructor(
    @param:S3UploadClient private val okHttpClient: OkHttpClient,
) : S3Repository {
    override suspend fun uploadImages(
        uploadInfos: List<PresignedUploadInfo>,
        files: List<File>,
        extensions: List<String>,
    ): Result<Unit> = runCatching {
        uploadInfos
            .zip(files.zip(extensions))
            .forEach { (info, pair) ->
                val (file, extension) = pair
                uploadSingleImage(info.url, file, extension).getOrThrow()
            }
    }

    override suspend fun uploadSingleImage(
        presignedUrl: String,
        file: File,
        extension: String,
    ): Result<Unit> = runCatching {
        val contentType = extension.toContentType()

        val requestBody = file.asRequestBody(contentType.toMediaType())

        val request = Request.Builder()
            .url(presignedUrl)
            .put(requestBody)
            .header("Content-Type", contentType)
            .build()

        okHttpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw IllegalStateException("S3 upload failed: ${response.code}")
            }
        }
    }
}

private fun String.toContentType(): String =
    when (lowercase()) {
        "jpg", "jpeg" -> "image/jpeg"
        "png" -> "image/png"
        "webp" -> "image/webp"
        else -> "application/octet-stream"
    }

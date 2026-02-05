package com.poti.android.data.repository

import com.poti.android.core.network.util.HttpResponseHandler
import com.poti.android.data.di.FileUploadClient
import com.poti.android.domain.model.image.PresignedUploadInfo
import com.poti.android.domain.repository.S3Repository
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class S3RepositoryImpl @Inject constructor(
    private val httpResponseHandler: HttpResponseHandler,
    @param:FileUploadClient private val okHttpClient: OkHttpClient,
) : S3Repository {
    override suspend fun uploadImages(
        uploadInfos: List<PresignedUploadInfo>,
        files: List<File>,
        extensions: List<String>,
    ): Result<Unit> = httpResponseHandler.safeApiCall {
        uploadInfos
            .zip(files.zip(extensions))
            .forEach { (info, pair) ->
                val (file, extension) = pair
                uploadSingleImageInternal(
                    presignedUrl = info.url,
                    file = file,
                    extension = extension,
                )
            }
    }

    override suspend fun uploadSingleImage(
        presignedUrl: String,
        file: File,
        extension: String,
    ): Result<Unit> = httpResponseHandler.safeApiCall {
        uploadSingleImageInternal(
            presignedUrl = presignedUrl,
            file = file,
            extension = extension,
        )
    }

    private fun uploadSingleImageInternal(
        presignedUrl: String,
        file: File,
        extension: String,
    ) {
        val contentType = extension.toContentType()
        val requestBody = file.asRequestBody(contentType.toMediaType())

        val request = Request.Builder()
            .url(presignedUrl)
            .put(requestBody)
            .header("Content-Type", contentType)
            .build()

        okHttpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw IllegalStateException(
                    "S3 upload failed: ${response.code}",
                )
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

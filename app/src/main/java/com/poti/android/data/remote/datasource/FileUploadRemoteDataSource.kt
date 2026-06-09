package com.poti.android.data.remote.datasource

import com.poti.android.core.common.constant.ImageConstants.IMAGE_CONTENT_TYPE
import com.poti.android.data.di.FileUploadClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class FileUploadRemoteDataSource @Inject constructor(
    @param:FileUploadClient private val okHttpClient: OkHttpClient,
) {
    suspend fun uploadImage(
        uploadUrl: String,
        file: File,
    ) = withContext(Dispatchers.IO) {
        val requestBody = file.asRequestBody(IMAGE_CONTENT_TYPE.toMediaType())

        val request = Request.Builder()
            .url(uploadUrl)
            .put(requestBody)
            .header("Content-Type", IMAGE_CONTENT_TYPE)
            .build()

        okHttpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw IllegalStateException(
                    "File upload failed: ${response.code}",
                )
            }
        }
    }
}

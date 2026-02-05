package com.poti.android.data.repository

import com.poti.android.core.network.util.HttpResponseHandler
import com.poti.android.data.local.datasource.FileLocalDataSource
import com.poti.android.data.remote.datasource.FileUploadRemoteDataSource
import com.poti.android.domain.repository.FileUploadRepository
import java.io.File
import javax.inject.Inject

class FileUploadRepositoryImpl @Inject constructor(
    private val httpResponseHandler: HttpResponseHandler,
    private val fileUplaodRemoteDataSource: FileUploadRemoteDataSource,
    private val fileLocalDataSource: FileLocalDataSource,
) : FileUploadRepository {
    override suspend fun uploadImage(
        uploadUrl: String,
        file: File,
    ): Result<Unit> = httpResponseHandler.safeApiCall {
        fileUplaodRemoteDataSource.uploadImage(uploadUrl, file)
    }

    override fun createImage(uriString: String): Result<File> {
        try {
            val file = fileLocalDataSource.createImageFile(uriString)
            return Result.success(file)
        } catch (exception: Throwable) {
            return Result.failure(exception)
        }
    }

    override fun clearDirectory(): Result<Unit> {
        try {
            fileLocalDataSource.clearDirectory()
            return Result.success(Unit)
        } catch (exception: Throwable) {
            return Result.failure(exception)
        }
    }
}

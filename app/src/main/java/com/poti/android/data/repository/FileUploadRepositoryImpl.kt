package com.poti.android.data.repository

import com.poti.android.core.common.util.suspendRunCatching
import com.poti.android.core.network.util.HttpResponseHandler
import com.poti.android.data.local.datasource.FileLocalDataSource
import com.poti.android.data.mock.executeWithUiMock
import com.poti.android.data.remote.datasource.FileUploadRemoteDataSource
import com.poti.android.domain.repository.FileUploadRepository
import java.io.File
import javax.inject.Inject

class FileUploadRepositoryImpl @Inject constructor(
    private val httpResponseHandler: HttpResponseHandler,
    private val fileUploadRemoteDataSource: FileUploadRemoteDataSource,
    private val fileLocalDataSource: FileLocalDataSource,
) : FileUploadRepository {
    override suspend fun uploadImage(
        uploadUrl: String,
        file: File,
    ): Result<Unit> = executeWithUiMock(
        mock = { Unit },
        real = {
            httpResponseHandler.safeApiCall {
                fileUploadRemoteDataSource.uploadImage(uploadUrl, file)
            }
        },
    )

    override suspend fun createImage(uriString: String): Result<File> = suspendRunCatching {
        fileLocalDataSource.createImageFile(uriString)
    }

    override suspend fun clearDirectory(): Result<Unit> = suspendRunCatching {
        fileLocalDataSource.clearDirectory()
    }
}

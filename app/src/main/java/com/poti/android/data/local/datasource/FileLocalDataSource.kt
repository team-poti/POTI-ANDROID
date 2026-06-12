package com.poti.android.data.local.datasource

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.util.Size
import androidx.core.net.toUri
import com.poti.android.core.common.constant.ImageConstants.IMAGE_EXTENSION
import com.poti.android.di.IoDispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.UUID
import javax.inject.Inject
import kotlin.math.max

class FileLocalDataSource @Inject constructor(
    @param:ApplicationContext private val context: Context,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) {
    suspend fun createImageFile(uriString: String): File = withContext(ioDispatcher) {
        val uri = uriString.toUri()
        val directory = getDirectory()
        compressImage(uri, directory)
    }

    suspend fun clearDirectory() = withContext(ioDispatcher) {
        val directory = getDirectory()
        directory.listFiles()?.forEach { it.delete() }
    }

    private fun getDirectory(): File = File(context.cacheDir, DIRECTORY).apply {
        mkdirs()
    }

    private fun compressImage(
        uri: Uri,
        dir: File,
    ): File {
        val source = ImageDecoder.createSource(context.contentResolver, uri)
        val bitmap = ImageDecoder.decodeBitmap(source) { decoder, info, _ ->
            // 디코더 설정
            decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
            decoder.isMutableRequired = true

            // 리사이징 크기 설정
            val targetSize = calculateTargetSize(info.size.width, info.size.height)
            decoder.setTargetSize(targetSize.width, targetSize.height)
        }

        // bitmap을 리사이징(압축)한 jpeg byteArray 생성
        val compressedImage = ByteArrayOutputStream().use { stream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, QUALITY, stream)
            bitmap.recycle()
            stream.toByteArray()
        }

        // 임시 파일 객체 생성
        val tempFile = File(
            dir,
            "${UUID.randomUUID()}.$IMAGE_EXTENSION",
        )

        // 파일 객채에 리사이징 byteArray 덮어씌워 최종 이미지 파일 생성
        tempFile.outputStream().use { stream ->
            stream.write(compressedImage)
        }

        return tempFile
    }

    private fun calculateTargetSize(
        width: Int,
        height: Int,
    ): Size {
        if (width <= MAX_WIDTH && height <= MAX_HEIGHT) return Size(width, height)

        val ratio = max(width.toFloat() / MAX_WIDTH, height.toFloat() / MAX_HEIGHT)

        return Size((width / ratio).toInt(), (height / ratio).toInt())
    }

    companion object {
        private const val MAX_WIDTH = 1024
        private const val MAX_HEIGHT = 1024
        private const val QUALITY = 80
        private const val DIRECTORY = "compressed"
    }
}

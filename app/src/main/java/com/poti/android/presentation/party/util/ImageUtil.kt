package com.poti.android.presentation.party.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.poti.android.domain.model.image.ImageInfoForPresigned
import java.io.File
import java.util.UUID

fun List<Uri>.toImageInfosForPresigned(context: Context): List<ImageInfoForPresigned> {
    return this.mapNotNull { uri -> uri.toImageInfoForPresigned(context) }
}

fun Uri.toImageInfoForPresigned(context: Context): ImageInfoForPresigned? {
    return context.contentResolver.query(
        this,
        null,
        null,
        null,
        null,
    )?.use { cursor ->
        if (!cursor.moveToFirst()) return@use null

        val nameIndex = cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME)
        val fileName = cursor.getString(nameIndex)
        val extension = fileName.substringAfterLast('.', "jpg")

        val tempFile = File(context.cacheDir, "upload_${UUID.randomUUID()}.$extension")
        context.contentResolver.openInputStream(this)?.use { input ->
            tempFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        ImageInfoForPresigned(
            file = tempFile,
            extension = extension,
        )
    }
}
